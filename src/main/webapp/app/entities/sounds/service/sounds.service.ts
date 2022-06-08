import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISounds, getSoundsIdentifier } from '../sounds.model';

export type EntityResponseType = HttpResponse<ISounds>;
export type EntityArrayResponseType = HttpResponse<ISounds[]>;

@Injectable({ providedIn: 'root' })
export class SoundsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sounds');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sounds: ISounds): Observable<EntityResponseType> {
    return this.http.post<ISounds>(this.resourceUrl, sounds, { observe: 'response' });
  }

  update(sounds: ISounds): Observable<EntityResponseType> {
    return this.http.put<ISounds>(`${this.resourceUrl}/${getSoundsIdentifier(sounds) as number}`, sounds, { observe: 'response' });
  }

  partialUpdate(sounds: ISounds): Observable<EntityResponseType> {
    return this.http.patch<ISounds>(`${this.resourceUrl}/${getSoundsIdentifier(sounds) as number}`, sounds, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISounds>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISounds[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoundsToCollectionIfMissing(soundsCollection: ISounds[], ...soundsToCheck: (ISounds | null | undefined)[]): ISounds[] {
    const sounds: ISounds[] = soundsToCheck.filter(isPresent);
    if (sounds.length > 0) {
      const soundsCollectionIdentifiers = soundsCollection.map(soundsItem => getSoundsIdentifier(soundsItem)!);
      const soundsToAdd = sounds.filter(soundsItem => {
        const soundsIdentifier = getSoundsIdentifier(soundsItem);
        if (soundsIdentifier == null || soundsCollectionIdentifiers.includes(soundsIdentifier)) {
          return false;
        }
        soundsCollectionIdentifiers.push(soundsIdentifier);
        return true;
      });
      return [...soundsToAdd, ...soundsCollection];
    }
    return soundsCollection;
  }
}
