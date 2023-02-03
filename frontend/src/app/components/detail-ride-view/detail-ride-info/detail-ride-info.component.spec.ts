import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailRideInfoComponent } from './detail-ride-info.component';

describe('DetailRideInfoComponent', () => {
  let component: DetailRideInfoComponent;
  let fixture: ComponentFixture<DetailRideInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailRideInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailRideInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
