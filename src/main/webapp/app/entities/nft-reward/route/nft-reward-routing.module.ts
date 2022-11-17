import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NftRewardComponent } from '../list/nft-reward.component';
import { NftRewardDetailComponent } from '../detail/nft-reward-detail.component';
import { NftRewardUpdateComponent } from '../update/nft-reward-update.component';
import { NftRewardRoutingResolveService } from './nft-reward-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const nftRewardRoute: Routes = [
  {
    path: '',
    component: NftRewardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NftRewardDetailComponent,
    resolve: {
      nftReward: NftRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NftRewardUpdateComponent,
    resolve: {
      nftReward: NftRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NftRewardUpdateComponent,
    resolve: {
      nftReward: NftRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nftRewardRoute)],
  exports: [RouterModule],
})
export class NftRewardRoutingModule {}
