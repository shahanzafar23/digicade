import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DailyRewardComponent } from './list/daily-reward.component';
import { DailyRewardDetailComponent } from './detail/daily-reward-detail.component';
import { DailyRewardUpdateComponent } from './update/daily-reward-update.component';
import { DailyRewardDeleteDialogComponent } from './delete/daily-reward-delete-dialog.component';
import { DailyRewardRoutingModule } from './route/daily-reward-routing.module';

@NgModule({
  imports: [SharedModule, DailyRewardRoutingModule],
  declarations: [DailyRewardComponent, DailyRewardDetailComponent, DailyRewardUpdateComponent, DailyRewardDeleteDialogComponent],
})
export class DailyRewardModule {}
