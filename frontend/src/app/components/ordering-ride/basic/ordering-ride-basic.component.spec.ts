import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderingRideBasicComponent } from './ordering-ride-basic.component';

describe('OrderingRideBasicComponent', () => {
  let component: OrderingRideBasicComponent;
  let fixture: ComponentFixture<OrderingRideBasicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderingRideBasicComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderingRideBasicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
