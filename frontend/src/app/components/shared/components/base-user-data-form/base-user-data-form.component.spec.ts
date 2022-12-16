import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseUserDataFormComponent } from './base-user-data-form.component';

describe('BaseUserDataFormComponent', () => {
  let component: BaseUserDataFormComponent;
  let fixture: ComponentFixture<BaseUserDataFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BaseUserDataFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BaseUserDataFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
