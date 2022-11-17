import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlayerNftRewardComponent } from '../list/player-nft-reward.component';
import { PlayerNftRewardDetailComponent } from '../detail/player-nft-reward-detail.component';
import { PlayerNftRewardUpdateComponent } from '../update/player-nft-reward-update.component';
import { PlayerNftRewardRoutingResolveService } from './player-nft-reward-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const playerNftRewardRoute: Routes = [
  {
    path: '',
    component: PlayerNftRewardComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlayerNftRewardDetailComponent,
    resolve: {
      playerNftReward: PlayerNftRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlayerNftRewardUpdateComponent,
    resolve: {
      playerNftReward: PlayerNftRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlayerNftRewardUpdateComponent,
    resolve: {
      playerNftReward: PlayerNftRewardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(playerNftRewardRoute)],
  exports: [RouterModule],
})
export class PlayerNftRewardRoutingModule {}
