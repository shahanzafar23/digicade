import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  dynamicSlides = [
    {
      id: '1' as string,
      src: '../../assets/img/main-page/black jack.svg',
      alt: 'Side 1',
      title: 'Side 1',
      url: 'https://www.free-fonts.com/square-721-bt-roman',
    },
    {
      id: '2' as string,
      src: '../../assets/img/main-page/runner.svg',
      alt: 'Side 2',
      title: 'Side 2',
      url: 'https://blog.logrocket.com/how-to-create-fancy-corners-in-css/',
    },
    {
      id: '3' as string,
      src: '../../assets/img/main-page/Slot.svg',
      alt: 'Side 3',
      title: 'Side 3',
      url: 'https://www.google.com/',
    },
    {
      id: '4' as string,
      src: '../../assets/img/main-page/match 3.svg',
      alt: 'Side 4',
      title: 'Side 4',
      url: 'https://www.npmjs.com/package/ngx-owl-carousel-o',
    },
    {
      id: '5' as string,
      src: '../../assets/img/main-page/Slot.svg',
      alt: 'Side 5',
      title: 'Side 5',
      url: 'https://owlcarousel2.github.io/OwlCarousel2/demos/autoplay.html',
    },
  ];

  customOptions: OwlOptions = {
    autoplay: true,
    autoplayHoverPause: true,
    loop: true,
    mouseDrag: true,
    touchDrag: true,
    pullDrag: true,
    dots: false,
    navText: ['&#8249', '&#8250;'],
    nav: false,
    responsive: {
      0: {
        items: 1,
      },
      400: {
        items: 2,
      },
      760: {
        items: 3,
      },
      1000: {
        items: 4,
      },
    },
  };

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
