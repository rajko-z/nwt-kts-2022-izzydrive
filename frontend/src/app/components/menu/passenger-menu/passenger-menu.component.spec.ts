import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerMenuComponent } from './passenger-menu.component';

describe('PassengerMenuComponent', () => {
  let component: PassengerMenuComponent;
  let fixture: ComponentFixture<PassengerMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PassengerMenuComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PassengerMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
