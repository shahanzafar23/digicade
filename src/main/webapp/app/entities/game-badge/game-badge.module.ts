import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GameBadgeComponent } from './list/game-badge.component';
import { GameBadgeDetailComponent } from './detail/game-badge-detail.component';
import { GameBadgeUpdateComponent } from './update/game-badge-update.component';
import { GameBadgeDeleteDialogComponent } from './delete/game-badge-delete-dialog.component';
import { GameBadgeRoutingModule } from './route/game-badge-routing.module';

@NgModule({
  imports: [SharedModule, GameBadgeRoutingModule],
  declarations: [GameBadgeComponent, GameBadgeDetailComponent, GameBadgeUpdateComponent, GameBadgeDeleteDialogComponent],
})
export class GameBadgeModule {}
