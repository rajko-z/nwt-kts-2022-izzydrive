import {Component, EventEmitter, Input, Output} from '@angular/core';
import {User} from 'src/app/model/user/user';
import {MatDialog} from "@angular/material/dialog";
import {ReviewRideTableComponent} from "../review-ride-table/review-ride-table.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LoggedUserService} from "../../services/loggedUser/logged-user.service";

@Component({
  selector: 'app-review-users-table',
  templateUrl: './review-users-table.component.html',
  styleUrls: ['./review-users-table.component.scss']
})
export class ReviewUsersTableComponent {

  displayedColumns: string[] = ['position', 'email', 'name', 'lastname', "phone", 'blocked', 'notes', 'rides'];

  @Input()
  users: User[];

  @Output() dataEvent = new EventEmitter<void>();

  constructor(private loggedUserService: LoggedUserService, public dialog: MatDialog, public snackBar: MatSnackBar) {
  }

  openDialog(user: User): void {
    this.dialog.open(ReviewRideTableComponent, {
      data: {id: user.id, name: user.firstName, lastname: user.lastName, role: user.role},
    });
  }

  isBlocked(id: string, blocked: boolean): void {
    if (!blocked) {
      this.blockUser(id);
    } else {
      this.unblockUser(id);
    }
  }

  blockUser(id: string): void {
    this.loggedUserService.blockUser(id).subscribe({
      next: res => this.emitResponse(res),
      error: error => this.snackBar.open(error.text, "ERROR", {
        duration: 2000,
      })
    });
  }

  unblockUser(id: string): void {
    this.loggedUserService.unblockUser(id).subscribe({
        next: res => this.emitResponse(res),
        error: error => this.snackBar.open(error.text, "ERROR", {
          duration: 2000,
        })
      }
    );
  }
  emitResponse(response:string):void{
    this.dataEvent.emit();
    this.snackBar.open(response, "OK", {
      duration: 2000,
    });
  }
}
