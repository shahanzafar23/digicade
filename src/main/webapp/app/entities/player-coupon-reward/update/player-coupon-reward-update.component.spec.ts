import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlayerCouponRewardFormService } from './player-coupon-reward-form.service';
import { PlayerCouponRewardService } from '../service/player-coupon-reward.service';
import { IPlayerCouponReward } from '../player-coupon-reward.model';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { ICouponReward } from 'app/entities/coupon-reward/coupon-reward.model';
import { CouponRewardService } from 'app/entities/coupon-reward/service/coupon-reward.service';

import { PlayerCouponRewardUpdateComponent } from './player-coupon-reward-update.component';

describe('PlayerCouponReward Management Update Component', () => {
  let comp: PlayerCouponRewardUpdateComponent;
  let fixture: ComponentFixture<PlayerCouponRewardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playerCouponRewardFormService: PlayerCouponRewardFormService;
  let playerCouponRewardService: PlayerCouponRewardService;
  let playerService: PlayerService;
  let couponRewardService: CouponRewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlayerCouponRewardUpdateComponent],
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
      .overrideTemplate(PlayerCouponRewardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayerCouponRewardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playerCouponRewardFormService = TestBed.inject(PlayerCouponRewardFormService);
    playerCouponRewardService = TestBed.inject(PlayerCouponRewardService);
    playerService = TestBed.inject(PlayerService);
    couponRewardService = TestBed.inject(CouponRewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Player query and add missing value', () => {
      const playerCouponReward: IPlayerCouponReward = { id: 456 };
      const player: IPlayer = { id: 61650 };
      playerCouponReward.player = player;

      const playerCollection: IPlayer[] = [{ id: 66008 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerCouponReward });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(
        playerCollection,
        ...additionalPlayers.map(expect.objectContaining)
      );
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CouponReward query and add missing value', () => {
      const playerCouponReward: IPlayerCouponReward = { id: 456 };
      const couponReward: ICouponReward = { id: 72091 };
      playerCouponReward.couponReward = couponReward;

      const couponRewardCollection: ICouponReward[] = [{ id: 52526 }];
      jest.spyOn(couponRewardService, 'query').mockReturnValue(of(new HttpResponse({ body: couponRewardCollection })));
      const additionalCouponRewards = [couponReward];
      const expectedCollection: ICouponReward[] = [...additionalCouponRewards, ...couponRewardCollection];
      jest.spyOn(couponRewardService, 'addCouponRewardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerCouponReward });
      comp.ngOnInit();

      expect(couponRewardService.query).toHaveBeenCalled();
      expect(couponRewardService.addCouponRewardToCollectionIfMissing).toHaveBeenCalledWith(
        couponRewardCollection,
        ...additionalCouponRewards.map(expect.objectContaining)
      );
      expect(comp.couponRewardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const playerCouponReward: IPlayerCouponReward = { id: 456 };
      const player: IPlayer = { id: 38535 };
      playerCouponReward.player = player;
      const couponReward: ICouponReward = { id: 48818 };
      playerCouponReward.couponReward = couponReward;

      activatedRoute.data = of({ playerCouponReward });
      comp.ngOnInit();

      expect(comp.playersSharedCollection).toContain(player);
      expect(comp.couponRewardsSharedCollection).toContain(couponReward);
      expect(comp.playerCouponReward).toEqual(playerCouponReward);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerCouponReward>>();
      const playerCouponReward = { id: 123 };
      jest.spyOn(playerCouponRewardFormService, 'getPlayerCouponReward').mockReturnValue(playerCouponReward);
      jest.spyOn(playerCouponRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerCouponReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerCouponReward }));
      saveSubject.complete();

      // THEN
      expect(playerCouponRewardFormService.getPlayerCouponReward).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playerCouponRewardService.update).toHaveBeenCalledWith(expect.objectContaining(playerCouponReward));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerCouponReward>>();
      const playerCouponReward = { id: 123 };
      jest.spyOn(playerCouponRewardFormService, 'getPlayerCouponReward').mockReturnValue({ id: null });
      jest.spyOn(playerCouponRewardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerCouponReward: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerCouponReward }));
      saveSubject.complete();

      // THEN
      expect(playerCouponRewardFormService.getPlayerCouponReward).toHaveBeenCalled();
      expect(playerCouponRewardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerCouponReward>>();
      const playerCouponReward = { id: 123 };
      jest.spyOn(playerCouponRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerCouponReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playerCouponRewardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlayer', () => {
      it('Should forward to playerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(playerService, 'comparePlayer');
        comp.comparePlayer(entity, entity2);
        expect(playerService.comparePlayer).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
