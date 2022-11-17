import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INftReward, NewNftReward } from '../nft-reward.model';

export type PartialUpdateNftReward = Partial<INftReward> & Pick<INftReward, 'id'>;

export type EntityResponseType = HttpResponse<INftReward>;
export type EntityArrayResponseType = HttpResponse<INftReward[]>;

@Injectable({ providedIn: 'root' })
export class NftRewardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nft-rewards');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nftReward: NewNftReward): Observable<EntityResponseType> {
    return this.http.post<INftReward>(this.resourceUrl, nftReward, { observe: 'response' });
  }

  update(nftReward: INftReward): Observable<EntityResponseType> {
    return this.http.put<INftReward>(`${this.resourceUrl}/${this.getNftRewardIdentifier(nftReward)}`, nftReward, { observe: 'response' });
  }

  partialUpdate(nftReward: PartialUpdateNftReward): Observable<EntityResponseType> {
    return this.http.patch<INftReward>(`${this.resourceUrl}/${this.getNftRewardIdentifier(nftReward)}`, nftReward, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INftReward>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INftReward[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNftRewardIdentifier(nftReward: Pick<INftReward, 'id'>): number {
    return nftReward.id;
  }

  compareNftReward(o1: Pick<INftReward, 'id'> | null, o2: Pick<INftReward, 'id'> | null): boolean {
    return o1 && o2 ? this.getNftRewardIdentifier(o1) === this.getNftRewardIdentifier(o2) : o1 === o2;
  }

  addNftRewardToCollectionIfMissing<Type extends Pick<INftReward, 'id'>>(
    nftRewardCollection: Type[],
    ...nftRewardsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const nftRewards: Type[] = nftRewardsToCheck.filter(isPresent);
    if (nftRewards.length > 0) {
      const nftRewardCollectionIdentifiers = nftRewardCollection.map(nftRewardItem => this.getNftRewardIdentifier(nftRewardItem)!);
      const nftRewardsToAdd = nftRewards.filter(nftRewardItem => {
        const nftRewardIdentifier = this.getNftRewardIdentifier(nftRewardItem);
        if (nftRewardCollectionIdentifiers.includes(nftRewardIdentifier)) {
          return false;
        }
        nftRewardCollectionIdentifiers.push(nftRewardIdentifier);
        return true;
      });
      return [...nftRewardsToAdd, ...nftRewardCollection];
    }
    return nftRewardCollection;
  }
}
