import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingReportComponent } from './driving-report.component';

describe('DrivingNumberReportComponent', () => {
  let component: DrivingReportComponent;
  let fixture: ComponentFixture<DrivingReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingReportComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
