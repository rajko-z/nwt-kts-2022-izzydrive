import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerOverviewComponent } from './passenger-overview.component';

describe('PassengerOverviewComponent', () => {
  let component: PassengerOverviewComponent;
  let fixture: ComponentFixture<PassengerOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PassengerOverviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
