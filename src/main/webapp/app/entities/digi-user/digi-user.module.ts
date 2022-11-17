import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DigiUserComponent } from './list/digi-user.component';
import { DigiUserDetailComponent } from './detail/digi-user-detail.component';
import { DigiUserUpdateComponent } from './update/digi-user-update.component';
import { DigiUserDeleteDialogComponent } from './delete/digi-user-delete-dialog.component';
import { DigiUserRoutingModule } from './route/digi-user-routing.module';

@NgModule({
  imports: [SharedModule, DigiUserRoutingModule],
  declarations: [DigiUserComponent, DigiUserDetailComponent, DigiUserUpdateComponent, DigiUserDeleteDialogComponent],
})
export class DigiUserModule {}
