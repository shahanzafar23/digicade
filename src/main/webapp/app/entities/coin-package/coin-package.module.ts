import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CoinPackageComponent } from './list/coin-package.component';
import { CoinPackageDetailComponent } from './detail/coin-package-detail.component';
import { CoinPackageUpdateComponent } from './update/coin-package-update.component';
import { CoinPackageDeleteDialogComponent } from './delete/coin-package-delete-dialog.component';
import { CoinPackageRoutingModule } from './route/coin-package-routing.module';

@NgModule({
  imports: [SharedModule, CoinPackageRoutingModule],
  declarations: [CoinPackageComponent, CoinPackageDetailComponent, CoinPackageUpdateComponent, CoinPackageDeleteDialogComponent],
})
export class CoinPackageModule {}
