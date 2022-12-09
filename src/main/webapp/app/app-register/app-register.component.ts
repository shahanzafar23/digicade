import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FacebookLoginProvider, GoogleLoginProvider, SocialAuthService } from '@abacritt/angularx-social-login';
import { AccountService } from '../core/auth/account.service';
import { LoginService } from '../login/login.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { RegisterService } from '../account/register/register.service';
import { Registration } from '../account/register/register.model';
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from '../config/error.constants';

@Component({
  selector: 'jhi-app-register',
  templateUrl: './app-register.component.html',
  styleUrls: ['./app-register.component.scss'],
})
export class AppRegisterComponent implements OnInit {
  authenticationError = false;

  user: any;
  loggedIn: any;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  registerForm = new FormGroup({
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(256)],
    }),
    lastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(256)],
    }),
    login: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
  });

  constructor(
    private router: Router,
    private accountService: AccountService,
    private loginService: LoginService,
    private authService: SocialAuthService,
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private registerService: RegisterService
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

  register(): void {
    console.log(this.registerForm.value);

    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const { password, confirmPassword } = this.registerForm.getRawValue();
    if (password !== confirmPassword) {
      this.doNotMatch = true;
    } else {
      const { firstName, lastName, login, email } = this.registerForm.getRawValue();
      this.registerService
        .save({ firstName, lastName, login, email, password, authorities: ['ROLE_USER'], langKey: 'en', activated: true })
        .subscribe({
          next: () => {
            this.success = true;
            this.router.navigate(['/login']);
          },
          error: response => this.processError(response),
        });
    }
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
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
