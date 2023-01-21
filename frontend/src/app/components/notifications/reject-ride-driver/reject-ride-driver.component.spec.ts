import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectRideDriverComponent } from './reject-ride-driver.component';

describe('RejectRideDriverComponent', () => {
  let component: RejectRideDriverComponent;
  let fixture: ComponentFixture<RejectRideDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectRideDriverComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RejectRideDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
