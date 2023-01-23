import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewAndWriteAdminNotesComponent } from './review-and-write-admin-notes.component';

describe('ReviewAndWriteAdminNotesComponent', () => {
  let component: ReviewAndWriteAdminNotesComponent;
  let fixture: ComponentFixture<ReviewAndWriteAdminNotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewAndWriteAdminNotesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewAndWriteAdminNotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
