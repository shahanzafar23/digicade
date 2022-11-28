import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../login/login.service';
import { SocialAuthService } from '@abacritt/angularx-social-login';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  constructor(private loginService: LoginService, private socialAuthService: SocialAuthService, private router: Router) {}

  ngOnInit(): void {}

  logout(): void {
    this.loginService.logout();
    this.socialAuthService.signOut();
    this.router.navigate(['/login']);
  }
}
