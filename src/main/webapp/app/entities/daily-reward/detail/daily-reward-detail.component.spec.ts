import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DailyRewardDetailComponent } from './daily-reward-detail.component';

describe('DailyReward Management Detail Component', () => {
  let comp: DailyRewardDetailComponent;
  let fixture: ComponentFixture<DailyRewardDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DailyRewardDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dailyReward: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DailyRewardDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DailyRewardDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dailyReward on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dailyReward).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
