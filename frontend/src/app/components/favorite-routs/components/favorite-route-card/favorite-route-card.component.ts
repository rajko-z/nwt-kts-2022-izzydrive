import {Component, Input} from '@angular/core';
import {RouteDTO} from 'src/app/model/route/route';

@Component({
  selector: 'app-favorite-route-card',
  templateUrl: './favorite-route-card.component.html',
  styleUrls: ['./favorite-route-card.component.scss']
})
export class FavoriteRouteCardComponent {

  constructor() { }
  @Input() favoriteRoute: RouteDTO;
  @Input() onRemoveFromFavorites : Function;
  @Input() onGetRide : Function;



}
