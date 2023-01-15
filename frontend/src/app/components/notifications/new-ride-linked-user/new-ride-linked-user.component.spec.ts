import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRideLinkedUserComponent } from './new-ride-linked-user.component';

describe('NewRideLinkedUserComponent', () => {
  let component: NewRideLinkedUserComponent;
  let fixture: ComponentFixture<NewRideLinkedUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewRideLinkedUserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewRideLinkedUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
