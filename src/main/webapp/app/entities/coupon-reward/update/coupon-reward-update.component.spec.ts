import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CouponRewardFormService } from './coupon-reward-form.service';
import { CouponRewardService } from '../service/coupon-reward.service';
import { ICouponReward } from '../coupon-reward.model';

import { CouponRewardUpdateComponent } from './coupon-reward-update.component';

describe('CouponReward Management Update Component', () => {
  let comp: CouponRewardUpdateComponent;
  let fixture: ComponentFixture<CouponRewardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let couponRewardFormService: CouponRewardFormService;
  let couponRewardService: CouponRewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CouponRewardUpdateComponent],
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
      .overrideTemplate(CouponRewardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CouponRewardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    couponRewardFormService = TestBed.inject(CouponRewardFormService);
    couponRewardService = TestBed.inject(CouponRewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const couponReward: ICouponReward = { id: 456 };

      activatedRoute.data = of({ couponReward });
      comp.ngOnInit();

      expect(comp.couponReward).toEqual(couponReward);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICouponReward>>();
      const couponReward = { id: 123 };
      jest.spyOn(couponRewardFormService, 'getCouponReward').mockReturnValue(couponReward);
      jest.spyOn(couponRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couponReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: couponReward }));
      saveSubject.complete();

      // THEN
      expect(couponRewardFormService.getCouponReward).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(couponRewardService.update).toHaveBeenCalledWith(expect.objectContaining(couponReward));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICouponReward>>();
      const couponReward = { id: 123 };
      jest.spyOn(couponRewardFormService, 'getCouponReward').mockReturnValue({ id: null });
      jest.spyOn(couponRewardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couponReward: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: couponReward }));
      saveSubject.complete();

      // THEN
      expect(couponRewardFormService.getCouponReward).toHaveBeenCalled();
      expect(couponRewardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICouponReward>>();
      const couponReward = { id: 123 };
      jest.spyOn(couponRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ couponReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(couponRewardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
