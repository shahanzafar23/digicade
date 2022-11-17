import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoinPackage, NewCoinPackage } from '../coin-package.model';

export type PartialUpdateCoinPackage = Partial<ICoinPackage> & Pick<ICoinPackage, 'id'>;

export type EntityResponseType = HttpResponse<ICoinPackage>;
export type EntityArrayResponseType = HttpResponse<ICoinPackage[]>;

@Injectable({ providedIn: 'root' })
export class CoinPackageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coin-packages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(coinPackage: NewCoinPackage): Observable<EntityResponseType> {
    return this.http.post<ICoinPackage>(this.resourceUrl, coinPackage, { observe: 'response' });
  }

  update(coinPackage: ICoinPackage): Observable<EntityResponseType> {
    return this.http.put<ICoinPackage>(`${this.resourceUrl}/${this.getCoinPackageIdentifier(coinPackage)}`, coinPackage, {
      observe: 'response',
    });
  }

  partialUpdate(coinPackage: PartialUpdateCoinPackage): Observable<EntityResponseType> {
    return this.http.patch<ICoinPackage>(`${this.resourceUrl}/${this.getCoinPackageIdentifier(coinPackage)}`, coinPackage, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICoinPackage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICoinPackage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCoinPackageIdentifier(coinPackage: Pick<ICoinPackage, 'id'>): number {
    return coinPackage.id;
  }

  compareCoinPackage(o1: Pick<ICoinPackage, 'id'> | null, o2: Pick<ICoinPackage, 'id'> | null): boolean {
    return o1 && o2 ? this.getCoinPackageIdentifier(o1) === this.getCoinPackageIdentifier(o2) : o1 === o2;
  }

  addCoinPackageToCollectionIfMissing<Type extends Pick<ICoinPackage, 'id'>>(
    coinPackageCollection: Type[],
    ...coinPackagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const coinPackages: Type[] = coinPackagesToCheck.filter(isPresent);
    if (coinPackages.length > 0) {
      const coinPackageCollectionIdentifiers = coinPackageCollection.map(
        coinPackageItem => this.getCoinPackageIdentifier(coinPackageItem)!
      );
      const coinPackagesToAdd = coinPackages.filter(coinPackageItem => {
        const coinPackageIdentifier = this.getCoinPackageIdentifier(coinPackageItem);
        if (coinPackageCollectionIdentifiers.includes(coinPackageIdentifier)) {
          return false;
        }
        coinPackageCollectionIdentifiers.push(coinPackageIdentifier);
        return true;
      });
      return [...coinPackagesToAdd, ...coinPackageCollection];
    }
    return coinPackageCollection;
  }
}
