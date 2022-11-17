import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayerCouponRewardDetailComponent } from './player-coupon-reward-detail.component';

describe('PlayerCouponReward Management Detail Component', () => {
  let comp: PlayerCouponRewardDetailComponent;
  let fixture: ComponentFixture<PlayerCouponRewardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlayerCouponRewardDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ playerCouponReward: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlayerCouponRewardDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlayerCouponRewardDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load playerCouponReward on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.playerCouponReward).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
