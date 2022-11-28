import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-app-register',
  templateUrl: './app-register.component.html',
  styleUrls: ['./app-register.component.scss'],
})
export class AppRegisterComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  goToFaq(): void {
    this.router.navigate(['/faq']);
  }
}
