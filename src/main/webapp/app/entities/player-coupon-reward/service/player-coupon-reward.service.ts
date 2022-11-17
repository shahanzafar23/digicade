import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayerCouponReward, NewPlayerCouponReward } from '../player-coupon-reward.model';

export type PartialUpdatePlayerCouponReward = Partial<IPlayerCouponReward> & Pick<IPlayerCouponReward, 'id'>;

type RestOf<T extends IPlayerCouponReward | NewPlayerCouponReward> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestPlayerCouponReward = RestOf<IPlayerCouponReward>;

export type NewRestPlayerCouponReward = RestOf<NewPlayerCouponReward>;

export type PartialUpdateRestPlayerCouponReward = RestOf<PartialUpdatePlayerCouponReward>;

export type EntityResponseType = HttpResponse<IPlayerCouponReward>;
export type EntityArrayResponseType = HttpResponse<IPlayerCouponReward[]>;

@Injectable({ providedIn: 'root' })
export class PlayerCouponRewardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/player-coupon-rewards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(playerCouponReward: NewPlayerCouponReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playerCouponReward);
    return this.http
      .post<RestPlayerCouponReward>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(playerCouponReward: IPlayerCouponReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playerCouponReward);
    return this.http
      .put<RestPlayerCouponReward>(`${this.resourceUrl}/${this.getPlayerCouponRewardIdentifier(playerCouponReward)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(playerCouponReward: PartialUpdatePlayerCouponReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playerCouponReward);
    return this.http
      .patch<RestPlayerCouponReward>(`${this.resourceUrl}/${this.getPlayerCouponRewardIdentifier(playerCouponReward)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlayerCouponReward>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlayerCouponReward[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlayerCouponRewardIdentifier(playerCouponReward: Pick<IPlayerCouponReward, 'id'>): number {
    return playerCouponReward.id;
  }

  comparePlayerCouponReward(o1: Pick<IPlayerCouponReward, 'id'> | null, o2: Pick<IPlayerCouponReward, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlayerCouponRewardIdentifier(o1) === this.getPlayerCouponRewardIdentifier(o2) : o1 === o2;
  }

  addPlayerCouponRewardToCollectionIfMissing<Type extends Pick<IPlayerCouponReward, 'id'>>(
    playerCouponRewardCollection: Type[],
    ...playerCouponRewardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playerCouponRewards: Type[] = playerCouponRewardsToCheck.filter(isPresent);
    if (playerCouponRewards.length > 0) {
      const playerCouponRewardCollectionIdentifiers = playerCouponRewardCollection.map(
        playerCouponRewardItem => this.getPlayerCouponRewardIdentifier(playerCouponRewardItem)!
      );
      const playerCouponRewardsToAdd = playerCouponRewards.filter(playerCouponRewardItem => {
        const playerCouponRewardIdentifier = this.getPlayerCouponRewardIdentifier(playerCouponRewardItem);
        if (playerCouponRewardCollectionIdentifiers.includes(playerCouponRewardIdentifier)) {
          return false;
        }
        playerCouponRewardCollectionIdentifiers.push(playerCouponRewardIdentifier);
        return true;
      });
      return [...playerCouponRewardsToAdd, ...playerCouponRewardCollection];
    }
    return playerCouponRewardCollection;
  }

  protected convertDateFromClient<T extends IPlayerCouponReward | NewPlayerCouponReward | PartialUpdatePlayerCouponReward>(
    playerCouponReward: T
  ): RestOf<T> {
    return {
      ...playerCouponReward,
      date: playerCouponReward.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPlayerCouponReward: RestPlayerCouponReward): IPlayerCouponReward {
    return {
      ...restPlayerCouponReward,
      date: restPlayerCouponReward.date ? dayjs(restPlayerCouponReward.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlayerCouponReward>): HttpResponse<IPlayerCouponReward> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlayerCouponReward[]>): HttpResponse<IPlayerCouponReward[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
