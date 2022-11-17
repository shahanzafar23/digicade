import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDailyReward, NewDailyReward } from '../daily-reward.model';

export type PartialUpdateDailyReward = Partial<IDailyReward> & Pick<IDailyReward, 'id'>;

export type EntityResponseType = HttpResponse<IDailyReward>;
export type EntityArrayResponseType = HttpResponse<IDailyReward[]>;

@Injectable({ providedIn: 'root' })
export class DailyRewardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/daily-rewards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dailyReward: NewDailyReward): Observable<EntityResponseType> {
    return this.http.post<IDailyReward>(this.resourceUrl, dailyReward, { observe: 'response' });
  }

  update(dailyReward: IDailyReward): Observable<EntityResponseType> {
    return this.http.put<IDailyReward>(`${this.resourceUrl}/${this.getDailyRewardIdentifier(dailyReward)}`, dailyReward, {
      observe: 'response',
    });
  }

  partialUpdate(dailyReward: PartialUpdateDailyReward): Observable<EntityResponseType> {
    return this.http.patch<IDailyReward>(`${this.resourceUrl}/${this.getDailyRewardIdentifier(dailyReward)}`, dailyReward, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDailyReward>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDailyReward[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDailyRewardIdentifier(dailyReward: Pick<IDailyReward, 'id'>): number {
    return dailyReward.id;
  }

  compareDailyReward(o1: Pick<IDailyReward, 'id'> | null, o2: Pick<IDailyReward, 'id'> | null): boolean {
    return o1 && o2 ? this.getDailyRewardIdentifier(o1) === this.getDailyRewardIdentifier(o2) : o1 === o2;
  }

  addDailyRewardToCollectionIfMissing<Type extends Pick<IDailyReward, 'id'>>(
    dailyRewardCollection: Type[],
    ...dailyRewardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dailyRewards: Type[] = dailyRewardsToCheck.filter(isPresent);
    if (dailyRewards.length > 0) {
      const dailyRewardCollectionIdentifiers = dailyRewardCollection.map(
        dailyRewardItem => this.getDailyRewardIdentifier(dailyRewardItem)!
      );
      const dailyRewardsToAdd = dailyRewards.filter(dailyRewardItem => {
        const dailyRewardIdentifier = this.getDailyRewardIdentifier(dailyRewardItem);
        if (dailyRewardCollectionIdentifiers.includes(dailyRewardIdentifier)) {
          return false;
        }
        dailyRewardCollectionIdentifiers.push(dailyRewardIdentifier);
        return true;
      });
      return [...dailyRewardsToAdd, ...dailyRewardCollection];
    }
    return dailyRewardCollection;
  }
}
