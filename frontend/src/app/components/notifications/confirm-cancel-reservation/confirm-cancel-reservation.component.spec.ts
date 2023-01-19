import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmCancelReservationComponent } from './confirm-cancel-reservation.component';

describe('ConfirmCancelReservationComponent', () => {
  let component: ConfirmCancelReservationComponent;
  let fixture: ComponentFixture<ConfirmCancelReservationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmCancelReservationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmCancelReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
