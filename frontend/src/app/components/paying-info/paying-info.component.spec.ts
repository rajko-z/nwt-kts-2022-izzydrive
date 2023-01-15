import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayingInfoComponent } from './paying-info.component';

describe('PayingInfoComponent', () => {
  let component: PayingInfoComponent;
  let fixture: ComponentFixture<PayingInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayingInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PayingInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
