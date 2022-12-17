import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-menu',
  templateUrl: './admin-menu.component.html',
  styleUrls: ['./admin-menu.component.scss', '../menu.component.scss']
})
export class AdminMenuComponent  {

  constructor(private router: Router) { }

  routAddDriver(){
    this.router.navigateByUrl("/profile/add-driver")
  }
  routAllDrivers(){
    this.router.navigateByUrl("/drivers")
  }

}
