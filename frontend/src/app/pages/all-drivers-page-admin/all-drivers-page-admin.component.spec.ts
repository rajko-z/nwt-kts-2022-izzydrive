import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllDriversPageAdminComponent } from './all-drivers-page-admin.component';

describe('AllDriversPageAdminComponent', () => {
  let component: AllDriversPageAdminComponent;
  let fixture: ComponentFixture<AllDriversPageAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllDriversPageAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllDriversPageAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
