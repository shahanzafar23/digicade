import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDigiUser } from '../digi-user.model';

@Component({
  selector: 'jhi-digi-user-detail',
  templateUrl: './digi-user-detail.component.html',
})
export class DigiUserDetailComponent implements OnInit {
  digiUser: IDigiUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ digiUser }) => {
      this.digiUser = digiUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
