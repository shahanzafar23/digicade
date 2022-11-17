import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayerNftReward, NewPlayerNftReward } from '../player-nft-reward.model';

export type PartialUpdatePlayerNftReward = Partial<IPlayerNftReward> & Pick<IPlayerNftReward, 'id'>;

type RestOf<T extends IPlayerNftReward | NewPlayerNftReward> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestPlayerNftReward = RestOf<IPlayerNftReward>;

export type NewRestPlayerNftReward = RestOf<NewPlayerNftReward>;

export type PartialUpdateRestPlayerNftReward = RestOf<PartialUpdatePlayerNftReward>;

export type EntityResponseType = HttpResponse<IPlayerNftReward>;
export type EntityArrayResponseType = HttpResponse<IPlayerNftReward[]>;

@Injectable({ providedIn: 'root' })
export class PlayerNftRewardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/player-nft-rewards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(playerNftReward: NewPlayerNftReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playerNftReward);
    return this.http
      .post<RestPlayerNftReward>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(playerNftReward: IPlayerNftReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playerNftReward);
    return this.http
      .put<RestPlayerNftReward>(`${this.resourceUrl}/${this.getPlayerNftRewardIdentifier(playerNftReward)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(playerNftReward: PartialUpdatePlayerNftReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playerNftReward);
    return this.http
      .patch<RestPlayerNftReward>(`${this.resourceUrl}/${this.getPlayerNftRewardIdentifier(playerNftReward)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlayerNftReward>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlayerNftReward[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlayerNftRewardIdentifier(playerNftReward: Pick<IPlayerNftReward, 'id'>): number {
    return playerNftReward.id;
  }

  comparePlayerNftReward(o1: Pick<IPlayerNftReward, 'id'> | null, o2: Pick<IPlayerNftReward, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlayerNftRewardIdentifier(o1) === this.getPlayerNftRewardIdentifier(o2) : o1 === o2;
  }

  addPlayerNftRewardToCollectionIfMissing<Type extends Pick<IPlayerNftReward, 'id'>>(
    playerNftRewardCollection: Type[],
    ...playerNftRewardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playerNftRewards: Type[] = playerNftRewardsToCheck.filter(isPresent);
    if (playerNftRewards.length > 0) {
      const playerNftRewardCollectionIdentifiers = playerNftRewardCollection.map(
        playerNftRewardItem => this.getPlayerNftRewardIdentifier(playerNftRewardItem)!
      );
      const playerNftRewardsToAdd = playerNftRewards.filter(playerNftRewardItem => {
        const playerNftRewardIdentifier = this.getPlayerNftRewardIdentifier(playerNftRewardItem);
        if (playerNftRewardCollectionIdentifiers.includes(playerNftRewardIdentifier)) {
          return false;
        }
        playerNftRewardCollectionIdentifiers.push(playerNftRewardIdentifier);
        return true;
      });
      return [...playerNftRewardsToAdd, ...playerNftRewardCollection];
    }
    return playerNftRewardCollection;
  }

  protected convertDateFromClient<T extends IPlayerNftReward | NewPlayerNftReward | PartialUpdatePlayerNftReward>(
    playerNftReward: T
  ): RestOf<T> {
    return {
      ...playerNftReward,
      date: playerNftReward.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPlayerNftReward: RestPlayerNftReward): IPlayerNftReward {
    return {
      ...restPlayerNftReward,
      date: restPlayerNftReward.date ? dayjs(restPlayerNftReward.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlayerNftReward>): HttpResponse<IPlayerNftReward> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlayerNftReward[]>): HttpResponse<IPlayerNftReward[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
