import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewRideTableComponent } from './review-ride-table.component';

describe('ReviewRideTableComponent', () => {
  let component: ReviewRideTableComponent;
  let fixture: ComponentFixture<ReviewRideTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewRideTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewRideTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
