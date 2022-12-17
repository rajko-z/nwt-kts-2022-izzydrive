import {Component, Input} from '@angular/core';
import { User } from 'src/app/model/user/user';
import {MatDialog} from "@angular/material/dialog";
import {ReviewRideTableComponent} from "../review-ride-table/review-ride-table.component";

@Component({
  selector: 'app-review-users-table',
  templateUrl: './review-users-table.component.html',
  styleUrls: ['./review-users-table.component.scss']
})
export class ReviewUsersTableComponent  {

  displayedColumns: string[] = ['position', 'email', 'name', 'lastname', "phone", 'blocked', 'notes', 'rides'];

  @Input()
  users : User[];

  constructor(public dialog: MatDialog) {}

  openDialog(user): void {
    console.log(user.id);
    this.dialog.open(ReviewRideTableComponent, {
      data: {id:user.id, name:user.firstName, lastname:user.lastName},
    });
  }
  isBlocked(): void {}

}
