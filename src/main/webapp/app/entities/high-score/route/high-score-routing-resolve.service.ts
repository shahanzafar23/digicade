import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHighScore } from '../high-score.model';
import { HighScoreService } from '../service/high-score.service';

@Injectable({ providedIn: 'root' })
export class HighScoreRoutingResolveService implements Resolve<IHighScore | null> {
  constructor(protected service: HighScoreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHighScore | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((highScore: HttpResponse<IHighScore>) => {
          if (highScore.body) {
            return of(highScore.body);
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
