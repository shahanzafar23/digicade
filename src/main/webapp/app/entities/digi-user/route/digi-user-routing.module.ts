import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DigiUserComponent } from '../list/digi-user.component';
import { DigiUserDetailComponent } from '../detail/digi-user-detail.component';
import { DigiUserUpdateComponent } from '../update/digi-user-update.component';
import { DigiUserRoutingResolveService } from './digi-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const digiUserRoute: Routes = [
  {
    path: '',
    component: DigiUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DigiUserDetailComponent,
    resolve: {
      digiUser: DigiUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DigiUserUpdateComponent,
    resolve: {
      digiUser: DigiUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DigiUserUpdateComponent,
    resolve: {
      digiUser: DigiUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(digiUserRoute)],
  exports: [RouterModule],
})
export class DigiUserRoutingModule {}
