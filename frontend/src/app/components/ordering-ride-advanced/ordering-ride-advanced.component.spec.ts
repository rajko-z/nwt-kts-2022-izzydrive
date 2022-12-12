import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderingRideAdvancedComponent } from './ordering-ride-advanced.component';

describe('OrderingRideAdvancedComponent', () => {
  let component: OrderingRideAdvancedComponent;
  let fixture: ComponentFixture<OrderingRideAdvancedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderingRideAdvancedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderingRideAdvancedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
