import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDigiUser } from '../digi-user.model';
import { DigiUserService } from '../service/digi-user.service';

@Injectable({ providedIn: 'root' })
export class DigiUserRoutingResolveService implements Resolve<IDigiUser | null> {
  constructor(protected service: DigiUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDigiUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((digiUser: HttpResponse<IDigiUser>) => {
          if (digiUser.body) {
            return of(digiUser.body);
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
