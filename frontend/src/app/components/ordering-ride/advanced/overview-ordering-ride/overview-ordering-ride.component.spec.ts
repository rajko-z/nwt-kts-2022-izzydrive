import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverviewOrderingRideComponent } from './overview-ordering-ride.component';

describe('OverviewOrderingRideComponent', () => {
  let component: OverviewOrderingRideComponent;
  let fixture: ComponentFixture<OverviewOrderingRideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OverviewOrderingRideComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OverviewOrderingRideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
