import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TransactionFormService, TransactionFormGroup } from './transaction-form.service';
import { ITransaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { ICoinPackage } from 'app/entities/coin-package/coin-package.model';
import { CoinPackageService } from 'app/entities/coin-package/service/coin-package.service';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  transaction: ITransaction | null = null;

  playersSharedCollection: IPlayer[] = [];
  coinPackagesSharedCollection: ICoinPackage[] = [];

  editForm: TransactionFormGroup = this.transactionFormService.createTransactionFormGroup();

  constructor(
    protected transactionService: TransactionService,
    protected transactionFormService: TransactionFormService,
    protected playerService: PlayerService,
    protected coinPackageService: CoinPackageService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePlayer = (o1: IPlayer | null, o2: IPlayer | null): boolean => this.playerService.comparePlayer(o1, o2);

  compareCoinPackage = (o1: ICoinPackage | null, o2: ICoinPackage | null): boolean => this.coinPackageService.compareCoinPackage(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      if (transaction) {
        this.updateForm(transaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.transactionFormService.getTransaction(this.editForm);
    if (transaction.id !== null) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.transaction = transaction;
    this.transactionFormService.resetForm(this.editForm, transaction);

    this.playersSharedCollection = this.playerService.addPlayerToCollectionIfMissing<IPlayer>(
      this.playersSharedCollection,
      transaction.player
    );
    this.coinPackagesSharedCollection = this.coinPackageService.addCoinPackageToCollectionIfMissing<ICoinPackage>(
      this.coinPackagesSharedCollection,
      transaction.coinPackage
    );
  }

  protected loadRelationshipsOptions(): void {
    this.playerService
      .query()
      .pipe(map((res: HttpResponse<IPlayer[]>) => res.body ?? []))
      .pipe(map((players: IPlayer[]) => this.playerService.addPlayerToCollectionIfMissing<IPlayer>(players, this.transaction?.player)))
      .subscribe((players: IPlayer[]) => (this.playersSharedCollection = players));

    this.coinPackageService
      .query()
      .pipe(map((res: HttpResponse<ICoinPackage[]>) => res.body ?? []))
      .pipe(
        map((coinPackages: ICoinPackage[]) =>
          this.coinPackageService.addCoinPackageToCollectionIfMissing<ICoinPackage>(coinPackages, this.transaction?.coinPackage)
        )
      )
      .subscribe((coinPackages: ICoinPackage[]) => (this.coinPackagesSharedCollection = coinPackages));
  }
}
