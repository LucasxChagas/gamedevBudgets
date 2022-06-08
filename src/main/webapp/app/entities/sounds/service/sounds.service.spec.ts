import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SoundTypes } from 'app/entities/enumerations/sound-types.model';
import { SoundFormats } from 'app/entities/enumerations/sound-formats.model';
import { ISounds, Sounds } from '../sounds.model';

import { SoundsService } from './sounds.service';

describe('Sounds Service', () => {
  let service: SoundsService;
  let httpMock: HttpTestingController;
  let elemDefault: ISounds;
  let expectedResult: ISounds | ISounds[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SoundsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      type: SoundTypes.SoundTrack,
      format: SoundFormats.MP3,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Sounds', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Sounds()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sounds', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          type: 'BBBBBB',
          format: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sounds', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          type: 'BBBBBB',
          format: 'BBBBBB',
        },
        new Sounds()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sounds', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          type: 'BBBBBB',
          format: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Sounds', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSoundsToCollectionIfMissing', () => {
      it('should add a Sounds to an empty array', () => {
        const sounds: ISounds = { id: 123 };
        expectedResult = service.addSoundsToCollectionIfMissing([], sounds);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sounds);
      });

      it('should not add a Sounds to an array that contains it', () => {
        const sounds: ISounds = { id: 123 };
        const soundsCollection: ISounds[] = [
          {
            ...sounds,
          },
          { id: 456 },
        ];
        expectedResult = service.addSoundsToCollectionIfMissing(soundsCollection, sounds);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sounds to an array that doesn't contain it", () => {
        const sounds: ISounds = { id: 123 };
        const soundsCollection: ISounds[] = [{ id: 456 }];
        expectedResult = service.addSoundsToCollectionIfMissing(soundsCollection, sounds);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sounds);
      });

      it('should add only unique Sounds to an array', () => {
        const soundsArray: ISounds[] = [{ id: 123 }, { id: 456 }, { id: 18770 }];
        const soundsCollection: ISounds[] = [{ id: 123 }];
        expectedResult = service.addSoundsToCollectionIfMissing(soundsCollection, ...soundsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sounds: ISounds = { id: 123 };
        const sounds2: ISounds = { id: 456 };
        expectedResult = service.addSoundsToCollectionIfMissing([], sounds, sounds2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sounds);
        expect(expectedResult).toContain(sounds2);
      });

      it('should accept null and undefined values', () => {
        const sounds: ISounds = { id: 123 };
        expectedResult = service.addSoundsToCollectionIfMissing([], null, sounds, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sounds);
      });

      it('should return initial array if no Sounds is added', () => {
        const soundsCollection: ISounds[] = [{ id: 123 }];
        expectedResult = service.addSoundsToCollectionIfMissing(soundsCollection, undefined, null);
        expect(expectedResult).toEqual(soundsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
