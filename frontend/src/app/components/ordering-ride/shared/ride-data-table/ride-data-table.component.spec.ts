import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideDataTableComponent } from './ride-data-table.component';

describe('RideDataTableComponent', () => {
  let component: RideDataTableComponent;
  let fixture: ComponentFixture<RideDataTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RideDataTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideDataTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
