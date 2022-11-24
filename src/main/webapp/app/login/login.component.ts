import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { FacebookLoginProvider, GoogleLoginProvider, SocialAuthService } from '@abacritt/angularx-social-login';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { Login } from './login.model';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    rememberMe: new FormControl(false, { nonNullable: true, validators: [Validators.required] }),
  });

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
      console.log('ONINIT');
      if (this.loggedIn) {
        this.loginWithOAuth();
      }
    });
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  login(): void {
    // console.log('Form details', this.loginForm.getRawValue());
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

  goToHome(): void {
    this.router.navigate(['/']);
  }
  goToRegister(): void {
    this.router.navigate(['/auth/register']);
  }
  goToFaq(): void {
    this.router.navigate(['faq']);
  }
}
