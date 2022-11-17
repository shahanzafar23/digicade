import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameLevel } from '../game-level.model';
import { GameLevelService } from '../service/game-level.service';

@Injectable({ providedIn: 'root' })
export class GameLevelRoutingResolveService implements Resolve<IGameLevel | null> {
  constructor(protected service: GameLevelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGameLevel | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gameLevel: HttpResponse<IGameLevel>) => {
          if (gameLevel.body) {
            return of(gameLevel.body);
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
