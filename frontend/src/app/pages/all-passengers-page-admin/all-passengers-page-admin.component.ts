import {Component, OnInit} from '@angular/core';
import {PassengerService} from 'src/app/services/passengerService/passenger.service';
import {User} from "../../model/user/user";

@Component({
  selector: 'app-all-passengers-page-admin',
  templateUrl: './all-passengers-page-admin.component.html',
  styleUrls: ['./all-passengers-page-admin.component.scss']
})
export class AllPassengersPageAdminComponent implements OnInit {

  users: User[];

  constructor(private passengerService: PassengerService) {
  }

  ngOnInit(): void {
    this.loadData()
  }

  refresh() {
    this.loadData();
  }

  loadData() {
    this.passengerService.findAll().subscribe((res) => {
      this.users = res as User[];
    });
  }
}
