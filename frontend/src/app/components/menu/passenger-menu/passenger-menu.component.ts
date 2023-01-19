import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-passenger-menu',
  templateUrl: './passenger-menu.component.html',
  styleUrls: ['./passenger-menu.component.scss', '../menu.component.scss']
})
export class PassengerMenuComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
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

  currentDrivingClicked() {
    this.router.navigateByUrl('/passenger/current-driving')
  }
}
