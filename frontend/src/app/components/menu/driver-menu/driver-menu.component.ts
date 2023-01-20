import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-driver-menu',
  templateUrl: './driver-menu.component.html',
  styleUrls: ['./driver-menu.component.scss', '../menu.component.scss']
})
export class DriverMenuComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }
  
  onDrivingHistory() {
    this.router.navigateByUrl("/user/driving-history");
  }

}
