import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PlayerFormService, PlayerFormGroup } from './player-form.service';
import { IPlayer } from '../player.model';
import { PlayerService } from '../service/player.service';
import { IDigiUser } from 'app/entities/digi-user/digi-user.model';
import { DigiUserService } from 'app/entities/digi-user/service/digi-user.service';

@Component({
  selector: 'jhi-player-update',
  templateUrl: './player-update.component.html',
})
export class PlayerUpdateComponent implements OnInit {
  isSaving = false;
  player: IPlayer | null = null;

  digiUsersCollection: IDigiUser[] = [];

  editForm: PlayerFormGroup = this.playerFormService.createPlayerFormGroup();

  constructor(
    protected playerService: PlayerService,
    protected playerFormService: PlayerFormService,
    protected digiUserService: DigiUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDigiUser = (o1: IDigiUser | null, o2: IDigiUser | null): boolean => this.digiUserService.compareDigiUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ player }) => {
      this.player = player;
      if (player) {
        this.updateForm(player);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const player = this.playerFormService.getPlayer(this.editForm);
    if (player.id !== null) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>): void {
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

  protected updateForm(player: IPlayer): void {
    this.player = player;
    this.playerFormService.resetForm(this.editForm, player);

    this.digiUsersCollection = this.digiUserService.addDigiUserToCollectionIfMissing<IDigiUser>(this.digiUsersCollection, player.digiUser);
  }

  protected loadRelationshipsOptions(): void {
    this.digiUserService
      .query({ filter: 'player-is-null' })
      .pipe(map((res: HttpResponse<IDigiUser[]>) => res.body ?? []))
      .pipe(
        map((digiUsers: IDigiUser[]) => this.digiUserService.addDigiUserToCollectionIfMissing<IDigiUser>(digiUsers, this.player?.digiUser))
      )
      .subscribe((digiUsers: IDigiUser[]) => (this.digiUsersCollection = digiUsers));
  }
}
