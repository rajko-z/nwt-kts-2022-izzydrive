import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingReportHeaderComponent } from './driving-report-header.component';

describe('DrivingReportHeaderComponent', () => {
  let component: DrivingReportHeaderComponent;
  let fixture: ComponentFixture<DrivingReportHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingReportHeaderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingReportHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
