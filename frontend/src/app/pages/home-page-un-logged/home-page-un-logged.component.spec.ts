import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePageUnLoggedComponent } from './home-page-un-logged.component';

describe('HomePageUnLoggedComponent', () => {
  let component: HomePageUnLoggedComponent;
  let fixture: ComponentFixture<HomePageUnLoggedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomePageUnLoggedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomePageUnLoggedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
