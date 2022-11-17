import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICouponImage } from '../coupon-image.model';
import { CouponImageService } from '../service/coupon-image.service';

@Injectable({ providedIn: 'root' })
export class CouponImageRoutingResolveService implements Resolve<ICouponImage | null> {
  constructor(protected service: CouponImageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICouponImage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((couponImage: HttpResponse<ICouponImage>) => {
          if (couponImage.body) {
            return of(couponImage.body);
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
