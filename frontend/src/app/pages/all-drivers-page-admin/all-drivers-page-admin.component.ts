import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user/user";
import {DriverService} from "../../services/driverService/driver.service";

@Component({
  selector: 'app-all-drivers-page-admin',
  templateUrl: './all-drivers-page-admin.component.html',
  styleUrls: ['./all-drivers-page-admin.component.scss']
})
export class AllDriversPageAdminComponent implements OnInit {

  users : User[];

  constructor(private driverService: DriverService) { }

  ngOnInit(): void {
    this.loadData();
  }
  refresh(){
    this.loadData();
  }
  loadData(){
    this.driverService.findAll().subscribe((res) => {
      this.users = res as User[];
    });
  }
}
