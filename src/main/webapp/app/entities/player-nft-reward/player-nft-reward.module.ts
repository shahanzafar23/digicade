import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlayerNftRewardComponent } from './list/player-nft-reward.component';
import { PlayerNftRewardDetailComponent } from './detail/player-nft-reward-detail.component';
import { PlayerNftRewardUpdateComponent } from './update/player-nft-reward-update.component';
import { PlayerNftRewardDeleteDialogComponent } from './delete/player-nft-reward-delete-dialog.component';
import { PlayerNftRewardRoutingModule } from './route/player-nft-reward-routing.module';

@NgModule({
  imports: [SharedModule, PlayerNftRewardRoutingModule],
  declarations: [
    PlayerNftRewardComponent,
    PlayerNftRewardDetailComponent,
    PlayerNftRewardUpdateComponent,
    PlayerNftRewardDeleteDialogComponent,
  ],
})
export class PlayerNftRewardModule {}
