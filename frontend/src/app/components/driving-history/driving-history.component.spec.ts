import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingHistoryComponent } from './driving-history.component';

describe('DrivingHistoryComponent', () => {
  let component: DrivingHistoryComponent;
  let fixture: ComponentFixture<DrivingHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
