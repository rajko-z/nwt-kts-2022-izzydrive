import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewUsersTableComponent } from './review-users-table.component';

describe('ReviewUsersTableComponent', () => {
  let component: ReviewUsersTableComponent;
  let fixture: ComponentFixture<ReviewUsersTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewUsersTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewUsersTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
