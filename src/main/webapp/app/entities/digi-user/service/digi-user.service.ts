import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDigiUser, NewDigiUser } from '../digi-user.model';

export type PartialUpdateDigiUser = Partial<IDigiUser> & Pick<IDigiUser, 'id'>;

type RestOf<T extends IDigiUser | NewDigiUser> = Omit<T, 'dob'> & {
  dob?: string | null;
};

export type RestDigiUser = RestOf<IDigiUser>;

export type NewRestDigiUser = RestOf<NewDigiUser>;

export type PartialUpdateRestDigiUser = RestOf<PartialUpdateDigiUser>;

export type EntityResponseType = HttpResponse<IDigiUser>;
export type EntityArrayResponseType = HttpResponse<IDigiUser[]>;

@Injectable({ providedIn: 'root' })
export class DigiUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/digi-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(digiUser: NewDigiUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(digiUser);
    return this.http
      .post<RestDigiUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(digiUser: IDigiUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(digiUser);
    return this.http
      .put<RestDigiUser>(`${this.resourceUrl}/${this.getDigiUserIdentifier(digiUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(digiUser: PartialUpdateDigiUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(digiUser);
    return this.http
      .patch<RestDigiUser>(`${this.resourceUrl}/${this.getDigiUserIdentifier(digiUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDigiUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDigiUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDigiUserIdentifier(digiUser: Pick<IDigiUser, 'id'>): number {
    return digiUser.id;
  }

  compareDigiUser(o1: Pick<IDigiUser, 'id'> | null, o2: Pick<IDigiUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getDigiUserIdentifier(o1) === this.getDigiUserIdentifier(o2) : o1 === o2;
  }

  addDigiUserToCollectionIfMissing<Type extends Pick<IDigiUser, 'id'>>(
    digiUserCollection: Type[],
    ...digiUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const digiUsers: Type[] = digiUsersToCheck.filter(isPresent);
    if (digiUsers.length > 0) {
      const digiUserCollectionIdentifiers = digiUserCollection.map(digiUserItem => this.getDigiUserIdentifier(digiUserItem)!);
      const digiUsersToAdd = digiUsers.filter(digiUserItem => {
        const digiUserIdentifier = this.getDigiUserIdentifier(digiUserItem);
        if (digiUserCollectionIdentifiers.includes(digiUserIdentifier)) {
          return false;
        }
        digiUserCollectionIdentifiers.push(digiUserIdentifier);
        return true;
      });
      return [...digiUsersToAdd, ...digiUserCollection];
    }
    return digiUserCollection;
  }

  protected convertDateFromClient<T extends IDigiUser | NewDigiUser | PartialUpdateDigiUser>(digiUser: T): RestOf<T> {
    return {
      ...digiUser,
      dob: digiUser.dob?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDigiUser: RestDigiUser): IDigiUser {
    return {
      ...restDigiUser,
      dob: restDigiUser.dob ? dayjs(restDigiUser.dob) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDigiUser>): HttpResponse<IDigiUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDigiUser[]>): HttpResponse<IDigiUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
