import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISounds, Sounds } from '../sounds.model';
import { SoundsService } from '../service/sounds.service';

@Injectable({ providedIn: 'root' })
export class SoundsRoutingResolveService implements Resolve<ISounds> {
  constructor(protected service: SoundsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISounds> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sounds: HttpResponse<Sounds>) => {
          if (sounds.body) {
            return of(sounds.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sounds());
  }
}
