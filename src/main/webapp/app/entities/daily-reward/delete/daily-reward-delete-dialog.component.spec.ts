jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DailyRewardService } from '../service/daily-reward.service';

import { DailyRewardDeleteDialogComponent } from './daily-reward-delete-dialog.component';

describe('DailyReward Management Delete Component', () => {
  let comp: DailyRewardDeleteDialogComponent;
  let fixture: ComponentFixture<DailyRewardDeleteDialogComponent>;
  let service: DailyRewardService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DailyRewardDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(DailyRewardDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DailyRewardDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DailyRewardService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
