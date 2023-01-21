import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationPageDriverComponent } from './reservation-page-driver.component';

describe('ReservationPageDriverComponent', () => {
  let component: ReservationPageDriverComponent;
  let fixture: ComponentFixture<ReservationPageDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReservationPageDriverComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationPageDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
