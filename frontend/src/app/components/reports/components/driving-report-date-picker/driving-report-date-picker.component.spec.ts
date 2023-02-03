import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingReportDatePickerComponent } from './driving-report-date-picker.component';

describe('DrivingReportDatePickerComponent', () => {
  let component: DrivingReportDatePickerComponent;
  let fixture: ComponentFixture<DrivingReportDatePickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingReportDatePickerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingReportDatePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
