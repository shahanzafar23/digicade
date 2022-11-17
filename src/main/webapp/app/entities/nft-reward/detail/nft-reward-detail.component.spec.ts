import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NftRewardDetailComponent } from './nft-reward-detail.component';

describe('NftReward Management Detail Component', () => {
  let comp: NftRewardDetailComponent;
  let fixture: ComponentFixture<NftRewardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NftRewardDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ nftReward: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NftRewardDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NftRewardDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load nftReward on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.nftReward).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
