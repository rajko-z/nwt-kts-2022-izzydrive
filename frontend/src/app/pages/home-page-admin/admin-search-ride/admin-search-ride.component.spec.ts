import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSearchRideComponent } from './admin-search-ride.component';

describe('AdminSearchRideComponent', () => {
  let component: AdminSearchRideComponent;
  let fixture: ComponentFixture<AdminSearchRideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminSearchRideComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSearchRideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
