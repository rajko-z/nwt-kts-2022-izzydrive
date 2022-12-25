import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherUsersDialogComponent } from './other-users-dialog.component';

describe('OtherUsersDialogComponent', () => {
  let component: OtherUsersDialogComponent;
  let fixture: ComponentFixture<OtherUsersDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OtherUsersDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OtherUsersDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
