import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoinPackageDetailComponent } from './coin-package-detail.component';

describe('CoinPackage Management Detail Component', () => {
  let comp: CoinPackageDetailComponent;
  let fixture: ComponentFixture<CoinPackageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CoinPackageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ coinPackage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CoinPackageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CoinPackageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load coinPackage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.coinPackage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
