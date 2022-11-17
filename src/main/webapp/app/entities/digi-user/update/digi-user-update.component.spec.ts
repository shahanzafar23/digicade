import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DigiUserFormService } from './digi-user-form.service';
import { DigiUserService } from '../service/digi-user.service';
import { IDigiUser } from '../digi-user.model';

import { DigiUserUpdateComponent } from './digi-user-update.component';

describe('DigiUser Management Update Component', () => {
  let comp: DigiUserUpdateComponent;
  let fixture: ComponentFixture<DigiUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let digiUserFormService: DigiUserFormService;
  let digiUserService: DigiUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DigiUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DigiUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DigiUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    digiUserFormService = TestBed.inject(DigiUserFormService);
    digiUserService = TestBed.inject(DigiUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const digiUser: IDigiUser = { id: 456 };

      activatedRoute.data = of({ digiUser });
      comp.ngOnInit();

      expect(comp.digiUser).toEqual(digiUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDigiUser>>();
      const digiUser = { id: 123 };
      jest.spyOn(digiUserFormService, 'getDigiUser').mockReturnValue(digiUser);
      jest.spyOn(digiUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ digiUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: digiUser }));
      saveSubject.complete();

      // THEN
      expect(digiUserFormService.getDigiUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(digiUserService.update).toHaveBeenCalledWith(expect.objectContaining(digiUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDigiUser>>();
      const digiUser = { id: 123 };
      jest.spyOn(digiUserFormService, 'getDigiUser').mockReturnValue({ id: null });
      jest.spyOn(digiUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ digiUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: digiUser }));
      saveSubject.complete();

      // THEN
      expect(digiUserFormService.getDigiUser).toHaveBeenCalled();
      expect(digiUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDigiUser>>();
      const digiUser = { id: 123 };
      jest.spyOn(digiUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ digiUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(digiUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
