import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilePageCarComponent } from './profile-page-car.component';

describe('ProfilePageCarComponent', () => {
  let component: ProfilePageCarComponent;
  let fixture: ComponentFixture<ProfilePageCarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfilePageCarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfilePageCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
