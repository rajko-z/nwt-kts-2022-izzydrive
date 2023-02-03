import { Component, Input, OnInit } from '@angular/core';
import { RouteDTO } from 'src/app/model/route/route';

@Component({
  selector: 'app-favorite-route-card',
  templateUrl: './favorite-route-card.component.html',
  styleUrls: ['./favorite-route-card.component.scss']
})
export class FavoriteRouteCardComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  @Input() favoriteRoute: RouteDTO;
  @Input() onRemoveFromFavorites : Function;
  @Input() onGetRide : Function;



}
