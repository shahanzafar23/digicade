import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoinPackage } from '../coin-package.model';

@Component({
  selector: 'jhi-coin-package-detail',
  templateUrl: './coin-package-detail.component.html',
})
export class CoinPackageDetailComponent implements OnInit {
  coinPackage: ICoinPackage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coinPackage }) => {
      this.coinPackage = coinPackage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
