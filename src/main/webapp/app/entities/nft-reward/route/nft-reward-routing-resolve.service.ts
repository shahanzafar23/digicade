import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INftReward } from '../nft-reward.model';
import { NftRewardService } from '../service/nft-reward.service';

@Injectable({ providedIn: 'root' })
export class NftRewardRoutingResolveService implements Resolve<INftReward | null> {
  constructor(protected service: NftRewardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INftReward | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nftReward: HttpResponse<INftReward>) => {
          if (nftReward.body) {
            return of(nftReward.body);
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
