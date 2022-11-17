import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CoinPackageFormService, CoinPackageFormGroup } from './coin-package-form.service';
import { ICoinPackage } from '../coin-package.model';
import { CoinPackageService } from '../service/coin-package.service';

@Component({
  selector: 'jhi-coin-package-update',
  templateUrl: './coin-package-update.component.html',
})
export class CoinPackageUpdateComponent implements OnInit {
  isSaving = false;
  coinPackage: ICoinPackage | null = null;

  editForm: CoinPackageFormGroup = this.coinPackageFormService.createCoinPackageFormGroup();

  constructor(
    protected coinPackageService: CoinPackageService,
    protected coinPackageFormService: CoinPackageFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coinPackage }) => {
      this.coinPackage = coinPackage;
      if (coinPackage) {
        this.updateForm(coinPackage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coinPackage = this.coinPackageFormService.getCoinPackage(this.editForm);
    if (coinPackage.id !== null) {
      this.subscribeToSaveResponse(this.coinPackageService.update(coinPackage));
    } else {
      this.subscribeToSaveResponse(this.coinPackageService.create(coinPackage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoinPackage>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(coinPackage: ICoinPackage): void {
    this.coinPackage = coinPackage;
    this.coinPackageFormService.resetForm(this.editForm, coinPackage);
  }
}
