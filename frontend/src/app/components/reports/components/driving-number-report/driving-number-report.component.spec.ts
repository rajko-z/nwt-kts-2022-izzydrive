import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingNumberReportComponent } from './driving-number-report.component';

describe('DrivingNumberReportComponent', () => {
  let component: DrivingNumberReportComponent;
  let fixture: ComponentFixture<DrivingNumberReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingNumberReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingNumberReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
