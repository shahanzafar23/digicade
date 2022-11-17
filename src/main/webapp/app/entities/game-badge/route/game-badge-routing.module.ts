import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GameBadgeComponent } from '../list/game-badge.component';
import { GameBadgeDetailComponent } from '../detail/game-badge-detail.component';
import { GameBadgeUpdateComponent } from '../update/game-badge-update.component';
import { GameBadgeRoutingResolveService } from './game-badge-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gameBadgeRoute: Routes = [
  {
    path: '',
    component: GameBadgeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GameBadgeDetailComponent,
    resolve: {
      gameBadge: GameBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GameBadgeUpdateComponent,
    resolve: {
      gameBadge: GameBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GameBadgeUpdateComponent,
    resolve: {
      gameBadge: GameBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gameBadgeRoute)],
  exports: [RouterModule],
})
export class GameBadgeRoutingModule {}
