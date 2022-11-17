import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DigiUserDetailComponent } from './digi-user-detail.component';

describe('DigiUser Management Detail Component', () => {
  let comp: DigiUserDetailComponent;
  let fixture: ComponentFixture<DigiUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DigiUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ digiUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DigiUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DigiUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load digiUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.digiUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
