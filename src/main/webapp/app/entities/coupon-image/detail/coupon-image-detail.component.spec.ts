import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CouponImageDetailComponent } from './coupon-image-detail.component';

describe('CouponImage Management Detail Component', () => {
  let comp: CouponImageDetailComponent;
  let fixture: ComponentFixture<CouponImageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CouponImageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ couponImage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CouponImageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CouponImageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load couponImage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.couponImage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
