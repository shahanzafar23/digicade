import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGameLevel } from '../game-level.model';

@Component({
  selector: 'jhi-game-level-detail',
  templateUrl: './game-level-detail.component.html',
})
export class GameLevelDetailComponent implements OnInit {
  gameLevel: IGameLevel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameLevel }) => {
      this.gameLevel = gameLevel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
