import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouriteRoutsComponent } from './favorite-routs.component';

describe('FavouriteRoutsComponent', () => {
  let component: FavouriteRoutsComponent;
  let fixture: ComponentFixture<FavouriteRoutsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavouriteRoutsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouriteRoutsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
