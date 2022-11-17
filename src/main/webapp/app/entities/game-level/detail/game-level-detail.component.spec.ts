import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GameLevelDetailComponent } from './game-level-detail.component';

describe('GameLevel Management Detail Component', () => {
  let comp: GameLevelDetailComponent;
  let fixture: ComponentFixture<GameLevelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GameLevelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gameLevel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GameLevelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GameLevelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gameLevel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gameLevel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
