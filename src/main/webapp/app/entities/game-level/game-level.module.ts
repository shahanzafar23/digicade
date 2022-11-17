import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GameLevelComponent } from './list/game-level.component';
import { GameLevelDetailComponent } from './detail/game-level-detail.component';
import { GameLevelUpdateComponent } from './update/game-level-update.component';
import { GameLevelDeleteDialogComponent } from './delete/game-level-delete-dialog.component';
import { GameLevelRoutingModule } from './route/game-level-routing.module';

@NgModule({
  imports: [SharedModule, GameLevelRoutingModule],
  declarations: [GameLevelComponent, GameLevelDetailComponent, GameLevelUpdateComponent, GameLevelDeleteDialogComponent],
})
export class GameLevelModule {}
