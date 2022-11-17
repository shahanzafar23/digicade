import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CouponRewardDetailComponent } from './coupon-reward-detail.component';

describe('CouponReward Management Detail Component', () => {
  let comp: CouponRewardDetailComponent;
  let fixture: ComponentFixture<CouponRewardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CouponRewardDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ couponReward: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CouponRewardDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CouponRewardDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load couponReward on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.couponReward).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
