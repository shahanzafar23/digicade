import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NftRewardComponent } from './list/nft-reward.component';
import { NftRewardDetailComponent } from './detail/nft-reward-detail.component';
import { NftRewardUpdateComponent } from './update/nft-reward-update.component';
import { NftRewardDeleteDialogComponent } from './delete/nft-reward-delete-dialog.component';
import { NftRewardRoutingModule } from './route/nft-reward-routing.module';

@NgModule({
  imports: [SharedModule, NftRewardRoutingModule],
  declarations: [NftRewardComponent, NftRewardDetailComponent, NftRewardUpdateComponent, NftRewardDeleteDialogComponent],
})
export class NftRewardModule {}
