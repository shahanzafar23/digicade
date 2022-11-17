import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayerNftRewardDetailComponent } from './player-nft-reward-detail.component';

describe('PlayerNftReward Management Detail Component', () => {
  let comp: PlayerNftRewardDetailComponent;
  let fixture: ComponentFixture<PlayerNftRewardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlayerNftRewardDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ playerNftReward: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlayerNftRewardDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlayerNftRewardDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load playerNftReward on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.playerNftReward).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
