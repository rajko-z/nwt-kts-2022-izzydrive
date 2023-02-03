import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-passenger-menu',
  templateUrl: './passenger-menu.component.html',
  styleUrls: ['./passenger-menu.component.scss', '../menu.component.scss']
})
export class PassengerMenuComponent {

  constructor(private router: Router) {
  }

  orderDrivingNowClicked() {
    this.router.navigateByUrl('/passenger/order-now')
  }

  orderDrivingForLaterClicked() {
    this.router.navigateByUrl('/passenger/order-for-later')
  }

  onDrivingHistory() {
    this.router.navigateByUrl("/user/driving-history");
  }

  onOpenReservations() {
    this.router.navigateByUrl("/passenger/reservations");
  }

  currentDrivingClicked() {
    this.router.navigateByUrl('/passenger/current-driving')
  }

  onFavoriteRouts() {
    this.router.navigateByUrl('/passenger/favorites')
  }
}
