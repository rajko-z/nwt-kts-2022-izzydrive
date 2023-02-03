import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationImageComponent } from './notification-image.component';

describe('NotificationImageComponent', () => {
  let component: NotificationImageComponent;
  let fixture: ComponentFixture<NotificationImageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotificationImageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
