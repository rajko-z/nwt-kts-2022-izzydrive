import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllPassengersPageAdminComponent } from './all-passengers-page-admin.component';

describe('AllPassengersPageAdminComponent', () => {
  let component: AllPassengersPageAdminComponent;
  let fixture: ComponentFixture<AllPassengersPageAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllPassengersPageAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllPassengersPageAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
