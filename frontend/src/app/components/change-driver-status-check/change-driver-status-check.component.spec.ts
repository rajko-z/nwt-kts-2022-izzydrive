import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeDriverStatusCheckComponent } from './change-driver-status-check.component';

describe('ChangeDriverStatusCheckComponent', () => {
  let component: ChangeDriverStatusCheckComponent;
  let fixture: ComponentFixture<ChangeDriverStatusCheckComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangeDriverStatusCheckComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangeDriverStatusCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
