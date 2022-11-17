import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GameBadgeDetailComponent } from './game-badge-detail.component';

describe('GameBadge Management Detail Component', () => {
  let comp: GameBadgeDetailComponent;
  let fixture: ComponentFixture<GameBadgeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GameBadgeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gameBadge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GameBadgeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GameBadgeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gameBadge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gameBadge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
