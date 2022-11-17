import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HighScoreComponent } from './list/high-score.component';
import { HighScoreDetailComponent } from './detail/high-score-detail.component';
import { HighScoreUpdateComponent } from './update/high-score-update.component';
import { HighScoreDeleteDialogComponent } from './delete/high-score-delete-dialog.component';
import { HighScoreRoutingModule } from './route/high-score-routing.module';

@NgModule({
  imports: [SharedModule, HighScoreRoutingModule],
  declarations: [HighScoreComponent, HighScoreDetailComponent, HighScoreUpdateComponent, HighScoreDeleteDialogComponent],
})
export class HighScoreModule {}
