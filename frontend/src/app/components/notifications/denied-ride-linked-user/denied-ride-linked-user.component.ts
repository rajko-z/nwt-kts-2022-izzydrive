import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";
import {Router} from "@angular/router";

@Component({
  selector: 'app-denied-ride-linked-user',
  templateUrl: './denied-ride-linked-user.component.html',
  styleUrls: ['./denied-ride-linked-user.component.scss']
})
export class DeniedRideLinkedUserComponent  {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data, private router : Router) {
  }

  okClick() {
    this.data.preClose();
    this.router.navigateByUrl('/passenger');
  }
}
