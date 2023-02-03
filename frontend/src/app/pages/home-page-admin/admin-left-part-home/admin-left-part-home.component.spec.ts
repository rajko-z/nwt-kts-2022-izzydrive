import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminLeftPartHomeComponent } from './admin-left-part-home.component';

describe('AdminLeftPartHomeComponent', () => {
  let component: AdminLeftPartHomeComponent;
  let fixture: ComponentFixture<AdminLeftPartHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminLeftPartHomeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminLeftPartHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
