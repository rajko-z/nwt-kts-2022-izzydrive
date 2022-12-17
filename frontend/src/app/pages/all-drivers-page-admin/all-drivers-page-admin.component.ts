import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user/user";
import {DriverService} from "../../services/driver/driver.service";

@Component({
  selector: 'app-all-drivers-page-admin',
  templateUrl: './all-drivers-page-admin.component.html',
  styleUrls: ['./all-drivers-page-admin.component.scss']
})
export class AllDriversPageAdminComponent implements OnInit {

  users : User[];

  constructor(private driverService: DriverService) { }

  ngOnInit(): void {
    this.driverService.findAll().subscribe((res) => {
      this.users = res as User[];
    });
  }
}
