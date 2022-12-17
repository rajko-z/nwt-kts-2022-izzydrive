import {Component, Input} from '@angular/core';
import { User } from 'src/app/model/user/user';
import {MatDialog} from "@angular/material/dialog";

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

  openDialog(id): void {
    console.log(id);
    this.dialog.open(ReviewRideTableComponent, {
      data: {id:id},
    });
  }
  isBlocked(): void {}

}
