import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { NftRewardFormService, NftRewardFormGroup } from './nft-reward-form.service';
import { INftReward } from '../nft-reward.model';
import { NftRewardService } from '../service/nft-reward.service';

@Component({
  selector: 'jhi-nft-reward-update',
  templateUrl: './nft-reward-update.component.html',
})
export class NftRewardUpdateComponent implements OnInit {
  isSaving = false;
  nftReward: INftReward | null = null;

  editForm: NftRewardFormGroup = this.nftRewardFormService.createNftRewardFormGroup();

  constructor(
    protected nftRewardService: NftRewardService,
    protected nftRewardFormService: NftRewardFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nftReward }) => {
      this.nftReward = nftReward;
      if (nftReward) {
        this.updateForm(nftReward);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nftReward = this.nftRewardFormService.getNftReward(this.editForm);
    if (nftReward.id !== null) {
      this.subscribeToSaveResponse(this.nftRewardService.update(nftReward));
    } else {
      this.subscribeToSaveResponse(this.nftRewardService.create(nftReward));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INftReward>>): void {
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

  protected updateForm(nftReward: INftReward): void {
    this.nftReward = nftReward;
    this.nftRewardFormService.resetForm(this.editForm, nftReward);
  }
}
