import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameBadge } from '../game-badge.model';
import { GameBadgeService } from '../service/game-badge.service';

@Injectable({ providedIn: 'root' })
export class GameBadgeRoutingResolveService implements Resolve<IGameBadge | null> {
  constructor(protected service: GameBadgeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGameBadge | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gameBadge: HttpResponse<IGameBadge>) => {
          if (gameBadge.body) {
            return of(gameBadge.body);
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
