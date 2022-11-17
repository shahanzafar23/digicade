import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HighScoreDetailComponent } from './high-score-detail.component';

describe('HighScore Management Detail Component', () => {
  let comp: HighScoreDetailComponent;
  let fixture: ComponentFixture<HighScoreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HighScoreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ highScore: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HighScoreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HighScoreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load highScore on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.highScore).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
