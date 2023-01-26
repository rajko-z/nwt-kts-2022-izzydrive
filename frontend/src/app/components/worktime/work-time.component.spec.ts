import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkTimeComponent } from './work-time.component';

describe('WorktimeComponent', () => {
  let component: WorkTimeComponent;
  let fixture: ComponentFixture<WorkTimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkTimeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
