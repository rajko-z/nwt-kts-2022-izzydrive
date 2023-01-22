import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentReservationComponent } from './payment-reservation.component';

describe('PaymentReservationComponent', () => {
  let component: PaymentReservationComponent;
  let fixture: ComponentFixture<PaymentReservationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentReservationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaymentReservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
