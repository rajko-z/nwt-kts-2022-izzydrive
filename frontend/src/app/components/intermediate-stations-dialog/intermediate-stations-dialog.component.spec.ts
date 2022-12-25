import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntermediateStationsDialogComponent } from './intermediate-stations-dialog.component';

describe('IntermediateStationsDialogComponent', () => {
  let component: IntermediateStationsDialogComponent;
  let fixture: ComponentFixture<IntermediateStationsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntermediateStationsDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IntermediateStationsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
