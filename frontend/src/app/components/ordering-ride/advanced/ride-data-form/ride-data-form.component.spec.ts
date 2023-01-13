import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideDataFormComponent } from './ride-data-form.component';

describe('RideDataFormComponent', () => {
  let component: RideDataFormComponent;
  let fixture: ComponentFixture<RideDataFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideDataFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideDataFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
