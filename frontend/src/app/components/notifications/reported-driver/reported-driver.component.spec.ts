import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportedDriverComponent } from './reported-driver.component';

describe('ReportedDriverComponent', () => {
  let component: ReportedDriverComponent;
  let fixture: ComponentFixture<ReportedDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportedDriverComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportedDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
