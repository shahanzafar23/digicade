import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICoinPackage } from '../coin-package.model';
import { CoinPackageService } from '../service/coin-package.service';

@Injectable({ providedIn: 'root' })
export class CoinPackageRoutingResolveService implements Resolve<ICoinPackage | null> {
  constructor(protected service: CoinPackageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICoinPackage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((coinPackage: HttpResponse<ICoinPackage>) => {
          if (coinPackage.body) {
            return of(coinPackage.body);
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
