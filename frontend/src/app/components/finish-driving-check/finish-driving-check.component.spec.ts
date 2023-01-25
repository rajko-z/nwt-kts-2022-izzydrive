import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinishDrivingCheckComponent } from './finish-driving-check.component';

describe('FinishDrivingCheckComponent', () => {
  let component: FinishDrivingCheckComponent;
  let fixture: ComponentFixture<FinishDrivingCheckComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinishDrivingCheckComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinishDrivingCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
