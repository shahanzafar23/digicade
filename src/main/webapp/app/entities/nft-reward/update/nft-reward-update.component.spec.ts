import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NftRewardFormService } from './nft-reward-form.service';
import { NftRewardService } from '../service/nft-reward.service';
import { INftReward } from '../nft-reward.model';

import { NftRewardUpdateComponent } from './nft-reward-update.component';

describe('NftReward Management Update Component', () => {
  let comp: NftRewardUpdateComponent;
  let fixture: ComponentFixture<NftRewardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let nftRewardFormService: NftRewardFormService;
  let nftRewardService: NftRewardService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NftRewardUpdateComponent],
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
      .overrideTemplate(NftRewardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NftRewardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nftRewardFormService = TestBed.inject(NftRewardFormService);
    nftRewardService = TestBed.inject(NftRewardService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const nftReward: INftReward = { id: 456 };

      activatedRoute.data = of({ nftReward });
      comp.ngOnInit();

      expect(comp.nftReward).toEqual(nftReward);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INftReward>>();
      const nftReward = { id: 123 };
      jest.spyOn(nftRewardFormService, 'getNftReward').mockReturnValue(nftReward);
      jest.spyOn(nftRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nftReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nftReward }));
      saveSubject.complete();

      // THEN
      expect(nftRewardFormService.getNftReward).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(nftRewardService.update).toHaveBeenCalledWith(expect.objectContaining(nftReward));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INftReward>>();
      const nftReward = { id: 123 };
      jest.spyOn(nftRewardFormService, 'getNftReward').mockReturnValue({ id: null });
      jest.spyOn(nftRewardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nftReward: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: nftReward }));
      saveSubject.complete();

      // THEN
      expect(nftRewardFormService.getNftReward).toHaveBeenCalled();
      expect(nftRewardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INftReward>>();
      const nftReward = { id: 123 };
      jest.spyOn(nftRewardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nftReward });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nftRewardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
