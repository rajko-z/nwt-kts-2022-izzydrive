import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteRouteDialogComponent } from './favorite-route-dialog.component';

describe('FavoriteRouteDialogComponent', () => {
  let component: FavoriteRouteDialogComponent;
  let fixture: ComponentFixture<FavoriteRouteDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavoriteRouteDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavoriteRouteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
