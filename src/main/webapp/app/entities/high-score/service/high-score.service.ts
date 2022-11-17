import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHighScore, NewHighScore } from '../high-score.model';

export type PartialUpdateHighScore = Partial<IHighScore> & Pick<IHighScore, 'id'>;

export type EntityResponseType = HttpResponse<IHighScore>;
export type EntityArrayResponseType = HttpResponse<IHighScore[]>;

@Injectable({ providedIn: 'root' })
export class HighScoreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/high-scores');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(highScore: NewHighScore): Observable<EntityResponseType> {
    return this.http.post<IHighScore>(this.resourceUrl, highScore, { observe: 'response' });
  }

  update(highScore: IHighScore): Observable<EntityResponseType> {
    return this.http.put<IHighScore>(`${this.resourceUrl}/${this.getHighScoreIdentifier(highScore)}`, highScore, { observe: 'response' });
  }

  partialUpdate(highScore: PartialUpdateHighScore): Observable<EntityResponseType> {
    return this.http.patch<IHighScore>(`${this.resourceUrl}/${this.getHighScoreIdentifier(highScore)}`, highScore, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHighScore>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHighScore[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHighScoreIdentifier(highScore: Pick<IHighScore, 'id'>): number {
    return highScore.id;
  }

  compareHighScore(o1: Pick<IHighScore, 'id'> | null, o2: Pick<IHighScore, 'id'> | null): boolean {
    return o1 && o2 ? this.getHighScoreIdentifier(o1) === this.getHighScoreIdentifier(o2) : o1 === o2;
  }

  addHighScoreToCollectionIfMissing<Type extends Pick<IHighScore, 'id'>>(
    highScoreCollection: Type[],
    ...highScoresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const highScores: Type[] = highScoresToCheck.filter(isPresent);
    if (highScores.length > 0) {
      const highScoreCollectionIdentifiers = highScoreCollection.map(highScoreItem => this.getHighScoreIdentifier(highScoreItem)!);
      const highScoresToAdd = highScores.filter(highScoreItem => {
        const highScoreIdentifier = this.getHighScoreIdentifier(highScoreItem);
        if (highScoreCollectionIdentifiers.includes(highScoreIdentifier)) {
          return false;
        }
        highScoreCollectionIdentifiers.push(highScoreIdentifier);
        return true;
      });
      return [...highScoresToAdd, ...highScoreCollection];
    }
    return highScoreCollection;
  }
}
