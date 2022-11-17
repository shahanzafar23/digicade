import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDailyReward } from '../daily-reward.model';
import { DailyRewardService } from '../service/daily-reward.service';

@Injectable({ providedIn: 'root' })
export class DailyRewardRoutingResolveService implements Resolve<IDailyReward | null> {
  constructor(protected service: DailyRewardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDailyReward | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dailyReward: HttpResponse<IDailyReward>) => {
          if (dailyReward.body) {
            return of(dailyReward.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
