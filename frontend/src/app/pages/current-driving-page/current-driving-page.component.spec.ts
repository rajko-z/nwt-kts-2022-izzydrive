import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrentDrivingPageComponent } from './current-driving-page.component';

describe('CurrentDrivingPageComponent', () => {
  let component: CurrentDrivingPageComponent;
  let fixture: ComponentFixture<CurrentDrivingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CurrentDrivingPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CurrentDrivingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
