import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideDataBasicFormComponent } from './ride-data-basic-form.component';

describe('RideDataBasicFormComponent', () => {
  let component: RideDataBasicFormComponent;
  let fixture: ComponentFixture<RideDataBasicFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideDataBasicFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideDataBasicFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
