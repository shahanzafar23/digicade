import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CouponImageFormService } from './coupon-image-form.service';
import { CouponImageService } from '../service/coupon-image.service';
import { ICouponImage } from '../coupon-image.model';
import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { CouponRewardService } from 'app/entities/coupon-reward/service/coupon-reward.service';

import { CouponImageUpdateComponent } from './coupon-image-update.component';

describe('CouponImage Management Update Component', () => {
  let comp: CouponImageUpdateComponent;
  let fixture: ComponentFixture<CouponImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let couponImageFormService: CouponImageFormService;
  let couponImageService: CouponImageService;
  let couponRewardService: CouponRewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CouponImageUpdateComponent],
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
      .overrideTemplate(CouponImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CouponImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    couponImageFormService = TestBed.inject(CouponImageFormService);
    couponImageService = TestBed.inject(CouponImageService);
    couponRewardService = TestBed.inject(CouponRewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CouponReward query and add missing value', () => {
      const couponImage: ICouponImage = { id: 456 };
      const couponReward: ICouponReward = { id: 90333 };
      couponImage.couponReward = couponReward;

      const couponRewardCollection: ICouponReward[] = [{ id: 42573 }];
      jest.spyOn(couponRewardService, 'query').mockReturnValue(of(new HttpResponse({ body: couponRewardCollection })));
      const additionalCouponRewards = [couponReward];
      const expectedCollection: ICouponReward[] = [...additionalCouponRewards, ...couponRewardCollection];
      jest.spyOn(couponRewardService, 'addCouponRewardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ couponImage });
      comp.ngOnInit();

      expect(couponRewardService.query).toHaveBeenCalled();
      expect(couponRewardService.addCouponRewardToCollectionIfMissing).toHaveBeenCalledWith(
        couponRewardCollection,
        ...additionalCouponRewards.map(expect.objectContaining)
      );
      expect(comp.couponRewardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const couponImage: ICouponImage = { id: 456 };
      const couponReward: ICouponReward = { id: 8734 };
      couponImage.couponReward = couponReward;

      activatedRoute.data = of({ couponImage });
      comp.ngOnInit();

      expect(comp.couponRewardsSharedCollection).toContain(couponReward);
      expect(comp.couponImage).toEqual(couponImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICouponImage>>();
      const couponImage = { id: 123 };
      jest.spyOn(couponImageFormService, 'getCouponImage').mockReturnValue(couponImage);
      jest.spyOn(couponImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couponImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: couponImage }));
      saveSubject.complete();

      // THEN
      expect(couponImageFormService.getCouponImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(couponImageService.update).toHaveBeenCalledWith(expect.objectContaining(couponImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICouponImage>>();
      const couponImage = { id: 123 };
      jest.spyOn(couponImageFormService, 'getCouponImage').mockReturnValue({ id: null });
      jest.spyOn(couponImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couponImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: couponImage }));
      saveSubject.complete();

      // THEN
      expect(couponImageFormService.getCouponImage).toHaveBeenCalled();
      expect(couponImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICouponImage>>();
      const couponImage = { id: 123 };
      jest.spyOn(couponImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couponImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(couponImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCouponReward', () => {
      it('Should forward to couponRewardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(couponRewardService, 'compareCouponReward');
        comp.compareCouponReward(entity, entity2);
        expect(couponRewardService.compareCouponReward).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
