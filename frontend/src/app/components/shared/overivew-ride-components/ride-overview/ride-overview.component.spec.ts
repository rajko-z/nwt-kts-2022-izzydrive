import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideOverviewComponent } from './ride-overview.component';

describe('RideOverviewComponent', () => {
  let component: RideOverviewComponent;
  let fixture: ComponentFixture<RideOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideOverviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
