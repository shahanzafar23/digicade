import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICouponImage, NewCouponImage } from '../coupon-image.model';

export type PartialUpdateCouponImage = Partial<ICouponImage> & Pick<ICouponImage, 'id'>;

export type EntityResponseType = HttpResponse<ICouponImage>;
export type EntityArrayResponseType = HttpResponse<ICouponImage[]>;

@Injectable({ providedIn: 'root' })
export class CouponImageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coupon-images');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(couponImage: NewCouponImage): Observable<EntityResponseType> {
    return this.http.post<ICouponImage>(this.resourceUrl, couponImage, { observe: 'response' });
  }

  update(couponImage: ICouponImage): Observable<EntityResponseType> {
    return this.http.put<ICouponImage>(`${this.resourceUrl}/${this.getCouponImageIdentifier(couponImage)}`, couponImage, {
      observe: 'response',
    });
  }

  partialUpdate(couponImage: PartialUpdateCouponImage): Observable<EntityResponseType> {
    return this.http.patch<ICouponImage>(`${this.resourceUrl}/${this.getCouponImageIdentifier(couponImage)}`, couponImage, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICouponImage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICouponImage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCouponImageIdentifier(couponImage: Pick<ICouponImage, 'id'>): number {
    return couponImage.id;
  }

  compareCouponImage(o1: Pick<ICouponImage, 'id'> | null, o2: Pick<ICouponImage, 'id'> | null): boolean {
    return o1 && o2 ? this.getCouponImageIdentifier(o1) === this.getCouponImageIdentifier(o2) : o1 === o2;
  }

  addCouponImageToCollectionIfMissing<Type extends Pick<ICouponImage, 'id'>>(
    couponImageCollection: Type[],
    ...couponImagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const couponImages: Type[] = couponImagesToCheck.filter(isPresent);
    if (couponImages.length > 0) {
      const couponImageCollectionIdentifiers = couponImageCollection.map(
        couponImageItem => this.getCouponImageIdentifier(couponImageItem)!
      );
      const couponImagesToAdd = couponImages.filter(couponImageItem => {
        const couponImageIdentifier = this.getCouponImageIdentifier(couponImageItem);
        if (couponImageCollectionIdentifiers.includes(couponImageIdentifier)) {
          return false;
        }
        couponImageCollectionIdentifiers.push(couponImageIdentifier);
        return true;
      });
      return [...couponImagesToAdd, ...couponImageCollection];
    }
    return couponImageCollection;
  }
}
