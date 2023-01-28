import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailRideViewComponent } from './detail-ride-view.component';

describe('DetailRideViewComponent', () => {
  let component: DetailRideViewComponent;
  let fixture: ComponentFixture<DetailRideViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailRideViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailRideViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
