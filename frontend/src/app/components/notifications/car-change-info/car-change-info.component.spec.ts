import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarChangeInfoComponent } from './car-change-info.component';

describe('CarChangeInfoComponent', () => {
  let component: CarChangeInfoComponent;
  let fixture: ComponentFixture<CarChangeInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CarChangeInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarChangeInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
