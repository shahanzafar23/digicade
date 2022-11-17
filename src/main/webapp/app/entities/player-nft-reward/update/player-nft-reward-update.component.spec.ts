import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlayerNftRewardFormService } from './player-nft-reward-form.service';
import { PlayerNftRewardService } from '../service/player-nft-reward.service';
import { IPlayerNftReward } from '../player-nft-reward.model';
import { IPlayer } from 'app/entities/player/player.model';
import { PlayerService } from 'app/entities/player/service/player.service';
import { INftReward } from 'app/entities/nft-reward/nft-reward.model';
import { NftRewardService } from 'app/entities/nft-reward/service/nft-reward.service';

import { PlayerNftRewardUpdateComponent } from './player-nft-reward-update.component';

describe('PlayerNftReward Management Update Component', () => {
  let comp: PlayerNftRewardUpdateComponent;
  let fixture: ComponentFixture<PlayerNftRewardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playerNftRewardFormService: PlayerNftRewardFormService;
  let playerNftRewardService: PlayerNftRewardService;
  let playerService: PlayerService;
  let nftRewardService: NftRewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlayerNftRewardUpdateComponent],
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
      .overrideTemplate(PlayerNftRewardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayerNftRewardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playerNftRewardFormService = TestBed.inject(PlayerNftRewardFormService);
    playerNftRewardService = TestBed.inject(PlayerNftRewardService);
    playerService = TestBed.inject(PlayerService);
    nftRewardService = TestBed.inject(NftRewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Player query and add missing value', () => {
      const playerNftReward: IPlayerNftReward = { id: 456 };
      const player: IPlayer = { id: 38340 };
      playerNftReward.player = player;

      const playerCollection: IPlayer[] = [{ id: 2533 }];
      jest.spyOn(playerService, 'query').mockReturnValue(of(new HttpResponse({ body: playerCollection })));
      const additionalPlayers = [player];
      const expectedCollection: IPlayer[] = [...additionalPlayers, ...playerCollection];
      jest.spyOn(playerService, 'addPlayerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerNftReward });
      comp.ngOnInit();

      expect(playerService.query).toHaveBeenCalled();
      expect(playerService.addPlayerToCollectionIfMissing).toHaveBeenCalledWith(
        playerCollection,
        ...additionalPlayers.map(expect.objectContaining)
      );
      expect(comp.playersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call NftReward query and add missing value', () => {
      const playerNftReward: IPlayerNftReward = { id: 456 };
      const nftReward: INftReward = { id: 72589 };
      playerNftReward.nftReward = nftReward;

      const nftRewardCollection: INftReward[] = [{ id: 79551 }];
      jest.spyOn(nftRewardService, 'query').mockReturnValue(of(new HttpResponse({ body: nftRewardCollection })));
      const additionalNftRewards = [nftReward];
      const expectedCollection: INftReward[] = [...additionalNftRewards, ...nftRewardCollection];
      jest.spyOn(nftRewardService, 'addNftRewardToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ playerNftReward });
      comp.ngOnInit();

      expect(nftRewardService.query).toHaveBeenCalled();
      expect(nftRewardService.addNftRewardToCollectionIfMissing).toHaveBeenCalledWith(
        nftRewardCollection,
        ...additionalNftRewards.map(expect.objectContaining)
      );
      expect(comp.nftRewardsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const playerNftReward: IPlayerNftReward = { id: 456 };
      const player: IPlayer = { id: 54200 };
      playerNftReward.player = player;
      const nftReward: INftReward = { id: 54461 };
      playerNftReward.nftReward = nftReward;

      activatedRoute.data = of({ playerNftReward });
      comp.ngOnInit();

      expect(comp.playersSharedCollection).toContain(player);
      expect(comp.nftRewardsSharedCollection).toContain(nftReward);
      expect(comp.playerNftReward).toEqual(playerNftReward);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerNftReward>>();
      const playerNftReward = { id: 123 };
      jest.spyOn(playerNftRewardFormService, 'getPlayerNftReward').mockReturnValue(playerNftReward);
      jest.spyOn(playerNftRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerNftReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerNftReward }));
      saveSubject.complete();

      // THEN
      expect(playerNftRewardFormService.getPlayerNftReward).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playerNftRewardService.update).toHaveBeenCalledWith(expect.objectContaining(playerNftReward));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerNftReward>>();
      const playerNftReward = { id: 123 };
      jest.spyOn(playerNftRewardFormService, 'getPlayerNftReward').mockReturnValue({ id: null });
      jest.spyOn(playerNftRewardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerNftReward: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playerNftReward }));
      saveSubject.complete();

      // THEN
      expect(playerNftRewardFormService.getPlayerNftReward).toHaveBeenCalled();
      expect(playerNftRewardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayerNftReward>>();
      const playerNftReward = { id: 123 };
      jest.spyOn(playerNftRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playerNftReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playerNftRewardService.update).toHaveBeenCalled();
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
