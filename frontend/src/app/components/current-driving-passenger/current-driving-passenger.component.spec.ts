import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentDrivingPassengerComponent } from './current-driving-passenger.component';

describe('CurrentDrivingPassengerComponent', () => {
  let component: CurrentDrivingPassengerComponent;
  let fixture: ComponentFixture<CurrentDrivingPassengerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrentDrivingPassengerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentDrivingPassengerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
