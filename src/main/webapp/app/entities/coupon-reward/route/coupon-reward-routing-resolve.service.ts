import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICouponReward } from '../coupon-reward.model';
import { CouponRewardService } from '../service/coupon-reward.service';

@Injectable({ providedIn: 'root' })
export class CouponRewardRoutingResolveService implements Resolve<ICouponReward | null> {
  constructor(protected service: CouponRewardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICouponReward | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((couponReward: HttpResponse<ICouponReward>) => {
          if (couponReward.body) {
            return of(couponReward.body);
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
