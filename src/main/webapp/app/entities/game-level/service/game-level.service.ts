import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameLevel, NewGameLevel } from '../game-level.model';

export type PartialUpdateGameLevel = Partial<IGameLevel> & Pick<IGameLevel, 'id'>;

export type EntityResponseType = HttpResponse<IGameLevel>;
export type EntityArrayResponseType = HttpResponse<IGameLevel[]>;

@Injectable({ providedIn: 'root' })
export class GameLevelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-levels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gameLevel: NewGameLevel): Observable<EntityResponseType> {
    return this.http.post<IGameLevel>(this.resourceUrl, gameLevel, { observe: 'response' });
  }

  update(gameLevel: IGameLevel): Observable<EntityResponseType> {
    return this.http.put<IGameLevel>(`${this.resourceUrl}/${this.getGameLevelIdentifier(gameLevel)}`, gameLevel, { observe: 'response' });
  }

  partialUpdate(gameLevel: PartialUpdateGameLevel): Observable<EntityResponseType> {
    return this.http.patch<IGameLevel>(`${this.resourceUrl}/${this.getGameLevelIdentifier(gameLevel)}`, gameLevel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGameLevel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGameLevel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameLevelIdentifier(gameLevel: Pick<IGameLevel, 'id'>): number {
    return gameLevel.id;
  }

  compareGameLevel(o1: Pick<IGameLevel, 'id'> | null, o2: Pick<IGameLevel, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameLevelIdentifier(o1) === this.getGameLevelIdentifier(o2) : o1 === o2;
  }

  addGameLevelToCollectionIfMissing<Type extends Pick<IGameLevel, 'id'>>(
    gameLevelCollection: Type[],
    ...gameLevelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameLevels: Type[] = gameLevelsToCheck.filter(isPresent);
    if (gameLevels.length > 0) {
      const gameLevelCollectionIdentifiers = gameLevelCollection.map(gameLevelItem => this.getGameLevelIdentifier(gameLevelItem)!);
      const gameLevelsToAdd = gameLevels.filter(gameLevelItem => {
        const gameLevelIdentifier = this.getGameLevelIdentifier(gameLevelItem);
        if (gameLevelCollectionIdentifiers.includes(gameLevelIdentifier)) {
          return false;
        }
        gameLevelCollectionIdentifiers.push(gameLevelIdentifier);
        return true;
      });
      return [...gameLevelsToAdd, ...gameLevelCollection];
    }
    return gameLevelCollection;
  }
}
