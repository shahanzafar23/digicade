import { Component, OnInit } from '@angular/core';
import { OwlOptions } from 'ngx-owl-carousel-o';

@Component({
  selector: 'jhi-app-carousel',
  templateUrl: './app-carousel.component.html',
  styleUrls: ['./app-carousel.component.scss'],
})
export class AppCarouselComponent implements OnInit {
  dynamicSlides = [
    {
      id: '1' as string,
      src: './content/img/main-page/runner.svg',
      alt: 'Side 2',
      title: 'Side 2',
      url: 'https://blog.logrocket.com/how-to-create-fancy-corners-in-css/',
    },
    {
      id: '2' as string,
      src: './content/img/main-page/Slot.svg',
      alt: 'Side 3',
      title: 'Side 3',
      url: 'https://www.google.com/',
    },
    {
      id: '3' as string,
      src: './content/img/main-page/match 3.svg',
      alt: 'Side 4',
      title: 'Side 4',
      url: 'https://www.npmjs.com/package/ngx-owl-carousel-o',
    },
    {
      id: '4' as string,
      src: './content/img/main-page/Slot.svg',
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

  constructor() {}

  ngOnInit(): void {}
}
