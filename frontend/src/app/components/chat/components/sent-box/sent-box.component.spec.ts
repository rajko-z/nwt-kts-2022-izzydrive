import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SentBoxComponent } from './sent-box.component';

describe('SentBoxComponent', () => {
  let component: SentBoxComponent;
  let fixture: ComponentFixture<SentBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SentBoxComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SentBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
