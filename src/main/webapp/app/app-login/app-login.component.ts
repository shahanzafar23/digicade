import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FacebookLoginProvider, GoogleLoginProvider, SocialAuthService } from '@abacritt/angularx-social-login';
import { AccountService } from '../core/auth/account.service';
import { LoginService } from '../login/login.service';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { Login } from '../login/login.model';

@Component({
  selector: 'jhi-app-login',
  templateUrl: './app-login.component.html',
  styleUrls: ['./app-login.component.scss'],
})
export class AppLoginComponent implements OnInit {
  authenticationError = false;

  user: any;
  loggedIn: any;

  email?: any;
  password?: any;
  rememberMe?: any;

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router,
    private authService: SocialAuthService,
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService
  ) {}

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });

    this.authService.authState.subscribe(user => {
      this.user = user;
      this.loggedIn = user != null;
      if (this.loggedIn) {
        this.loginWithOAuth();
      }
    });
  }

  login(): void {
    let login = new Login(this.email, this.password, false);
    console.log('Form details', login);
    this.loginService.login(login).subscribe({
      next: () => {
        this.authenticationError = false;
        if (!this.router.getCurrentNavigation()) {
          // There were no routing during login (eg from navigationToStoredUrl)
          this.router.navigate(['']);
        }
      },
      error: () => (this.authenticationError = true),
    });
  }

  loginWithOAuth(): void {
    console.log(this.user);

    let user: any = {
      firstName: this.user['firstName'],
      lastName: this.user['lastName'],
      email: this.user['email'],
      activated: true,
      imageUrl: this.user['photoUrl'],
      authorities: ['ROLE_USER'],
      password: '',
      login: this.user['email'],
      token: this.user['idToken'],
      provider: this.user['provider'],
    };

    this.http.get(this.applicationConfigService.getEndpointFor('oauth/login') + '/' + this.user['email']).subscribe({
      next: (credential: any) => {
        if (credential !== null) {
          let loginPassword = '';
          if (credential.provider === 'GOOGLE') {
            loginPassword = 'GOOGLE';
          } else if (credential.provider === 'GOOGLE') {
            loginPassword = 'FACEBOOK';
          }
          this.loginService.login({ username: this.user['email'], password: loginPassword, rememberMe: false }).subscribe({
            next: () => {
              this.authenticationError = false;
              if (!this.router.getCurrentNavigation()) {
                // There were no routing during login (eg from navigationToStoredUrl)
                this.router.navigate(['']);
              }
            },
            error: () => (this.authenticationError = true),
          });
        } else {
          this.registerUser(user);
        }
      },
      error: () => {},
    });
  }

  registerUser(user: any): void {
    this.http.post(this.applicationConfigService.getEndpointFor('oauth/register'), user).subscribe({
      next: (credential: any) => {
        let loginPassword = '';
        if (user.provider === 'GOOGLE') {
          loginPassword = 'GOOGLE';
        } else if (user.provider === 'GOOGLE') {
          loginPassword = 'FACEBOOK';
        }
        this.loginService.login({ username: credential.email, password: loginPassword, rememberMe: false }).subscribe({
          next: () => {
            this.authenticationError = false;
            if (!this.router.getCurrentNavigation()) {
              // There were no routing during login (eg from navigationToStoredUrl)
              this.router.navigate(['']);
            }
          },
          error: () => (this.authenticationError = true),
        });
      },
      error: () => {
        console.log('User not registered');
      },
    });
  }

  signInWithGoogle(): void {
    console.log('Login with google');
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  signInWithFB(): void {
    console.log('Login with facebook');
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

  signOut(): void {
    this.authService.signOut();
  }
}
