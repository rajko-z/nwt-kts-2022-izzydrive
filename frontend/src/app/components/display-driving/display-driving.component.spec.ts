import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayDrivingComponent } from './display-driving.component';

describe('DisplayDrivingComponent', () => {
  let component: DisplayDrivingComponent;
  let fixture: ComponentFixture<DisplayDrivingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DisplayDrivingComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DisplayDrivingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
