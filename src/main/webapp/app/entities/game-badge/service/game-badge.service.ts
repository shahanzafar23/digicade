import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameBadge, NewGameBadge } from '../game-badge.model';

export type PartialUpdateGameBadge = Partial<IGameBadge> & Pick<IGameBadge, 'id'>;

export type EntityResponseType = HttpResponse<IGameBadge>;
export type EntityArrayResponseType = HttpResponse<IGameBadge[]>;

@Injectable({ providedIn: 'root' })
export class GameBadgeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-badges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gameBadge: NewGameBadge): Observable<EntityResponseType> {
    return this.http.post<IGameBadge>(this.resourceUrl, gameBadge, { observe: 'response' });
  }

  update(gameBadge: IGameBadge): Observable<EntityResponseType> {
    return this.http.put<IGameBadge>(`${this.resourceUrl}/${this.getGameBadgeIdentifier(gameBadge)}`, gameBadge, { observe: 'response' });
  }

  partialUpdate(gameBadge: PartialUpdateGameBadge): Observable<EntityResponseType> {
    return this.http.patch<IGameBadge>(`${this.resourceUrl}/${this.getGameBadgeIdentifier(gameBadge)}`, gameBadge, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGameBadge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGameBadge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameBadgeIdentifier(gameBadge: Pick<IGameBadge, 'id'>): number {
    return gameBadge.id;
  }

  compareGameBadge(o1: Pick<IGameBadge, 'id'> | null, o2: Pick<IGameBadge, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameBadgeIdentifier(o1) === this.getGameBadgeIdentifier(o2) : o1 === o2;
  }

  addGameBadgeToCollectionIfMissing<Type extends Pick<IGameBadge, 'id'>>(
    gameBadgeCollection: Type[],
    ...gameBadgesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameBadges: Type[] = gameBadgesToCheck.filter(isPresent);
    if (gameBadges.length > 0) {
      const gameBadgeCollectionIdentifiers = gameBadgeCollection.map(gameBadgeItem => this.getGameBadgeIdentifier(gameBadgeItem)!);
      const gameBadgesToAdd = gameBadges.filter(gameBadgeItem => {
        const gameBadgeIdentifier = this.getGameBadgeIdentifier(gameBadgeItem);
        if (gameBadgeCollectionIdentifiers.includes(gameBadgeIdentifier)) {
          return false;
        }
        gameBadgeCollectionIdentifiers.push(gameBadgeIdentifier);
        return true;
      });
      return [...gameBadgesToAdd, ...gameBadgeCollection];
    }
    return gameBadgeCollection;
  }
}
