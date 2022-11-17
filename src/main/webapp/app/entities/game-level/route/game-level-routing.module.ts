import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GameLevelComponent } from '../list/game-level.component';
import { GameLevelDetailComponent } from '../detail/game-level-detail.component';
import { GameLevelUpdateComponent } from '../update/game-level-update.component';
import { GameLevelRoutingResolveService } from './game-level-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gameLevelRoute: Routes = [
  {
    path: '',
    component: GameLevelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GameLevelDetailComponent,
    resolve: {
      gameLevel: GameLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GameLevelUpdateComponent,
    resolve: {
      gameLevel: GameLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GameLevelUpdateComponent,
    resolve: {
      gameLevel: GameLevelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gameLevelRoute)],
  exports: [RouterModule],
})
export class GameLevelRoutingModule {}
