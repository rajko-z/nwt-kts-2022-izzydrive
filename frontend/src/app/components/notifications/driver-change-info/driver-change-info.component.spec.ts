import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriverChangeInfoComponent } from './driver-change-info.component';

describe('DriverChangeInfoComponent', () => {
  let component: DriverChangeInfoComponent;
  let fixture: ComponentFixture<DriverChangeInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DriverChangeInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DriverChangeInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
