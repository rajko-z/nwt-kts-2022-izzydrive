import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteRouteCardComponent } from './favorite-route-card.component';

describe('FavoriteRouteCardComponent', () => {
  let component: FavoriteRouteCardComponent;
  let fixture: ComponentFixture<FavoriteRouteCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavoriteRouteCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavoriteRouteCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
