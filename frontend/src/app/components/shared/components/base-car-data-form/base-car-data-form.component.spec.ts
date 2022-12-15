import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseCarDataFormComponent } from './base-car-data-form.component';

describe('BaseCarDataFormComponent', () => {
  let component: BaseCarDataFormComponent;
  let fixture: ComponentFixture<BaseCarDataFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BaseCarDataFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BaseCarDataFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
