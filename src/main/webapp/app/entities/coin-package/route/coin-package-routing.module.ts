import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CoinPackageComponent } from '../list/coin-package.component';
import { CoinPackageDetailComponent } from '../detail/coin-package-detail.component';
import { CoinPackageUpdateComponent } from '../update/coin-package-update.component';
import { CoinPackageRoutingResolveService } from './coin-package-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const coinPackageRoute: Routes = [
  {
    path: '',
    component: CoinPackageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CoinPackageDetailComponent,
    resolve: {
      coinPackage: CoinPackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CoinPackageUpdateComponent,
    resolve: {
      coinPackage: CoinPackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CoinPackageUpdateComponent,
    resolve: {
      coinPackage: CoinPackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(coinPackageRoute)],
  exports: [RouterModule],
})
export class CoinPackageRoutingModule {}
