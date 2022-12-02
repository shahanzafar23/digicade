import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../login/login.service';
import { SocialAuthService } from '@abacritt/angularx-social-login';
import { Router } from '@angular/router';
import { AccountService } from '../../core/auth/account.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  isSignedIn: boolean = false;

  constructor(
    private loginService: LoginService,
    private socialAuthService: SocialAuthService,
    private router: Router,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.isSignedIn = true;
      }
    });
  }

  logout(): void {
    this.loginService.logout();
    this.socialAuthService.signOut();
    this.router.navigate(['/login']);
  }
}
