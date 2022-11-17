import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayerNftReward } from '../player-nft-reward.model';
import { PlayerNftRewardService } from '../service/player-nft-reward.service';

@Injectable({ providedIn: 'root' })
export class PlayerNftRewardRoutingResolveService implements Resolve<IPlayerNftReward | null> {
  constructor(protected service: PlayerNftRewardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayerNftReward | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((playerNftReward: HttpResponse<IPlayerNftReward>) => {
          if (playerNftReward.body) {
            return of(playerNftReward.body);
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
