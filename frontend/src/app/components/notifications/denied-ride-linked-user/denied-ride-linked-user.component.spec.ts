import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeniedRideLinkedUserComponent } from './denied-ride-linked-user.component';

describe('DeniedRideLinkedUserComponent', () => {
  let component: DeniedRideLinkedUserComponent;
  let fixture: ComponentFixture<DeniedRideLinkedUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeniedRideLinkedUserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeniedRideLinkedUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
