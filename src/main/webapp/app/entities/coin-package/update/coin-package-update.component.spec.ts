import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CoinPackageFormService } from './coin-package-form.service';
import { CoinPackageService } from '../service/coin-package.service';
import { ICoinPackage } from '../coin-package.model';

import { CoinPackageUpdateComponent } from './coin-package-update.component';

describe('CoinPackage Management Update Component', () => {
  let comp: CoinPackageUpdateComponent;
  let fixture: ComponentFixture<CoinPackageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coinPackageFormService: CoinPackageFormService;
  let coinPackageService: CoinPackageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CoinPackageUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CoinPackageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoinPackageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coinPackageFormService = TestBed.inject(CoinPackageFormService);
    coinPackageService = TestBed.inject(CoinPackageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const coinPackage: ICoinPackage = { id: 456 };

      activatedRoute.data = of({ coinPackage });
      comp.ngOnInit();

      expect(comp.coinPackage).toEqual(coinPackage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoinPackage>>();
      const coinPackage = { id: 123 };
      jest.spyOn(coinPackageFormService, 'getCoinPackage').mockReturnValue(coinPackage);
      jest.spyOn(coinPackageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coinPackage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coinPackage }));
      saveSubject.complete();

      // THEN
      expect(coinPackageFormService.getCoinPackage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(coinPackageService.update).toHaveBeenCalledWith(expect.objectContaining(coinPackage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoinPackage>>();
      const coinPackage = { id: 123 };
      jest.spyOn(coinPackageFormService, 'getCoinPackage').mockReturnValue({ id: null });
      jest.spyOn(coinPackageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coinPackage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coinPackage }));
      saveSubject.complete();

      // THEN
      expect(coinPackageFormService.getCoinPackage).toHaveBeenCalled();
      expect(coinPackageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoinPackage>>();
      const coinPackage = { id: 123 };
      jest.spyOn(coinPackageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coinPackage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coinPackageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
