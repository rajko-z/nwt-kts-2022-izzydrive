import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportDriverCheckComponent } from './report-driver-check.component';

describe('ReportDriverCheckComponent', () => {
  let component: ReportDriverCheckComponent;
  let fixture: ComponentFixture<ReportDriverCheckComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportDriverCheckComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportDriverCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
