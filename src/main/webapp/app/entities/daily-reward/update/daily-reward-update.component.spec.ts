import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DailyRewardFormService } from './daily-reward-form.service';
import { DailyRewardService } from '../service/daily-reward.service';
import { IDailyReward } from '../daily-reward.model';
import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { CouponRewardService } from 'app/entities/coupon-reward/service/coupon-reward.service';
import { INftReward } from 'app/entities/nft-reward/nft-reward.model';
import { NftRewardService } from 'app/entities/nft-reward/service/nft-reward.service';

import { DailyRewardUpdateComponent } from './daily-reward-update.component';

describe('DailyReward Management Update Component', () => {
  let comp: DailyRewardUpdateComponent;
  let fixture: ComponentFixture<DailyRewardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dailyRewardFormService: DailyRewardFormService;
  let dailyRewardService: DailyRewardService;
  let couponRewardService: CouponRewardService;
  let nftRewardService: NftRewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DailyRewardUpdateComponent],
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
      .overrideTemplate(DailyRewardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DailyRewardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dailyRewardFormService = TestBed.inject(DailyRewardFormService);
    dailyRewardService = TestBed.inject(DailyRewardService);
    couponRewardService = TestBed.inject(CouponRewardService);
    nftRewardService = TestBed.inject(NftRewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CouponReward query and add missing value', () => {
      const dailyReward: IDailyReward = { id: 456 };
      const couponReward: ICouponReward = { id: 92838 };
      dailyReward.couponReward = couponReward;

      const couponRewardCollection: ICouponReward[] = [{ id: 4185 }];
      jest.spyOn(couponRewardService, 'query').mockReturnValue(of(new HttpResponse({ body: couponRewardCollection })));
      const additionalCouponRewards = [couponReward];
      const expectedCollection: ICouponReward[] = [...additionalCouponRewards, ...couponRewardCollection];
      jest.spyOn(couponRewardService, 'addCouponRewardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dailyReward });
      comp.ngOnInit();

      expect(couponRewardService.query).toHaveBeenCalled();
      expect(couponRewardService.addCouponRewardToCollectionIfMissing).toHaveBeenCalledWith(
        couponRewardCollection,
        ...additionalCouponRewards.map(expect.objectContaining)
      );
      expect(comp.couponRewardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call NftReward query and add missing value', () => {
      const dailyReward: IDailyReward = { id: 456 };
      const nftReward: INftReward = { id: 11525 };
      dailyReward.nftReward = nftReward;

      const nftRewardCollection: INftReward[] = [{ id: 42690 }];
      jest.spyOn(nftRewardService, 'query').mockReturnValue(of(new HttpResponse({ body: nftRewardCollection })));
      const additionalNftRewards = [nftReward];
      const expectedCollection: INftReward[] = [...additionalNftRewards, ...nftRewardCollection];
      jest.spyOn(nftRewardService, 'addNftRewardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dailyReward });
      comp.ngOnInit();

      expect(nftRewardService.query).toHaveBeenCalled();
      expect(nftRewardService.addNftRewardToCollectionIfMissing).toHaveBeenCalledWith(
        nftRewardCollection,
        ...additionalNftRewards.map(expect.objectContaining)
      );
      expect(comp.nftRewardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dailyReward: IDailyReward = { id: 456 };
      const couponReward: ICouponReward = { id: 77151 };
      dailyReward.couponReward = couponReward;
      const nftReward: INftReward = { id: 37691 };
      dailyReward.nftReward = nftReward;

      activatedRoute.data = of({ dailyReward });
      comp.ngOnInit();

      expect(comp.couponRewardsSharedCollection).toContain(couponReward);
      expect(comp.nftRewardsSharedCollection).toContain(nftReward);
      expect(comp.dailyReward).toEqual(dailyReward);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDailyReward>>();
      const dailyReward = { id: 123 };
      jest.spyOn(dailyRewardFormService, 'getDailyReward').mockReturnValue(dailyReward);
      jest.spyOn(dailyRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dailyReward }));
      saveSubject.complete();

      // THEN
      expect(dailyRewardFormService.getDailyReward).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dailyRewardService.update).toHaveBeenCalledWith(expect.objectContaining(dailyReward));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDailyReward>>();
      const dailyReward = { id: 123 };
      jest.spyOn(dailyRewardFormService, 'getDailyReward').mockReturnValue({ id: null });
      jest.spyOn(dailyRewardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyReward: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dailyReward }));
      saveSubject.complete();

      // THEN
      expect(dailyRewardFormService.getDailyReward).toHaveBeenCalled();
      expect(dailyRewardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDailyReward>>();
      const dailyReward = { id: 123 };
      jest.spyOn(dailyRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dailyReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dailyRewardService.update).toHaveBeenCalled();
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

    describe('compareNftReward', () => {
      it('Should forward to nftRewardService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(nftRewardService, 'compareNftReward');
        comp.compareNftReward(entity, entity2);
        expect(nftRewardService.compareNftReward).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
