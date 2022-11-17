import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICouponReward, NewCouponReward } from '../coupon-reward.model';

export type PartialUpdateCouponReward = Partial<ICouponReward> & Pick<ICouponReward, 'id'>;

type RestOf<T extends ICouponReward | NewCouponReward> = Omit<T, 'expiry'> & {
  expiry?: string | null;
};

export type RestCouponReward = RestOf<ICouponReward>;

export type NewRestCouponReward = RestOf<NewCouponReward>;

export type PartialUpdateRestCouponReward = RestOf<PartialUpdateCouponReward>;

export type EntityResponseType = HttpResponse<ICouponReward>;
export type EntityArrayResponseType = HttpResponse<ICouponReward[]>;

@Injectable({ providedIn: 'root' })
export class CouponRewardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coupon-rewards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(couponReward: NewCouponReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(couponReward);
    return this.http
      .post<RestCouponReward>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(couponReward: ICouponReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(couponReward);
    return this.http
      .put<RestCouponReward>(`${this.resourceUrl}/${this.getCouponRewardIdentifier(couponReward)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(couponReward: PartialUpdateCouponReward): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(couponReward);
    return this.http
      .patch<RestCouponReward>(`${this.resourceUrl}/${this.getCouponRewardIdentifier(couponReward)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCouponReward>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCouponReward[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCouponRewardIdentifier(couponReward: Pick<ICouponReward, 'id'>): number {
    return couponReward.id;
  }

  compareCouponReward(o1: Pick<ICouponReward, 'id'> | null, o2: Pick<ICouponReward, 'id'> | null): boolean {
    return o1 && o2 ? this.getCouponRewardIdentifier(o1) === this.getCouponRewardIdentifier(o2) : o1 === o2;
  }

  addCouponRewardToCollectionIfMissing<Type extends Pick<ICouponReward, 'id'>>(
    couponRewardCollection: Type[],
    ...couponRewardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const couponRewards: Type[] = couponRewardsToCheck.filter(isPresent);
    if (couponRewards.length > 0) {
      const couponRewardCollectionIdentifiers = couponRewardCollection.map(
        couponRewardItem => this.getCouponRewardIdentifier(couponRewardItem)!
      );
      const couponRewardsToAdd = couponRewards.filter(couponRewardItem => {
        const couponRewardIdentifier = this.getCouponRewardIdentifier(couponRewardItem);
        if (couponRewardCollectionIdentifiers.includes(couponRewardIdentifier)) {
          return false;
        }
        couponRewardCollectionIdentifiers.push(couponRewardIdentifier);
        return true;
      });
      return [...couponRewardsToAdd, ...couponRewardCollection];
    }
    return couponRewardCollection;
  }

  protected convertDateFromClient<T extends ICouponReward | NewCouponReward | PartialUpdateCouponReward>(couponReward: T): RestOf<T> {
    return {
      ...couponReward,
      expiry: couponReward.expiry?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCouponReward: RestCouponReward): ICouponReward {
    return {
      ...restCouponReward,
      expiry: restCouponReward.expiry ? dayjs(restCouponReward.expiry) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCouponReward>): HttpResponse<ICouponReward> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCouponReward[]>): HttpResponse<ICouponReward[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
