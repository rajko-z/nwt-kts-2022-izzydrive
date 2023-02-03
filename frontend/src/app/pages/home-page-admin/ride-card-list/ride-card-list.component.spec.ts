import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideCardListComponent } from './ride-card-list.component';

describe('RideCardListComponent', () => {
  let component: RideCardListComponent;
  let fixture: ComponentFixture<RideCardListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideCardListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideCardListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
