import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHighScore } from '../high-score.model';

@Component({
  selector: 'jhi-high-score-detail',
  templateUrl: './high-score-detail.component.html',
})
export class HighScoreDetailComponent implements OnInit {
  highScore: IHighScore | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ highScore }) => {
      this.highScore = highScore;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
