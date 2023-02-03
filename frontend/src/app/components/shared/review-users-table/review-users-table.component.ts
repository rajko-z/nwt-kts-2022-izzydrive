import {Component, EventEmitter, Input, Output} from '@angular/core';
import {User} from 'src/app/model/user/user';
import {MatDialog} from "@angular/material/dialog";
import {ReviewRideTableComponent} from "../review-ride-table/review-ride-table.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LoggedUserService} from "../../../services/loggedUser/logged-user.service";
import {
  ReviewAndWriteAdminNotesComponent
} from "../../review-and-write-admin-notes/review-and-write-admin-notes.component";
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

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

  constructor(private loggedUserService: LoggedUserService, 
              public dialog: MatDialog, 
              public snackBar: MatSnackBar,
              private reponseMessage: ResponseMessageService) {
  }

  openRideDialog(user: User): void {
    this.dialog.open(ReviewRideTableComponent, {
      data: {
        id: user.id,
        firstName: user.firstName,
        lastName: user.lastName,
        role: user.role,
        message: 'The user does not have his own ride!'
      },
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
      error: error => this.reponseMessage.openErrorMessage(error.text)
    });
  }

  unblockUser(id: string): void {
    this.loggedUserService.unblockUser(id).subscribe({
        next: res => this.emitResponse(res),
        error: error => this.reponseMessage.openErrorMessage(error.text)
      }
    );
  }

  emitResponse(response: string): void {
    this.dataEvent.emit();
    this.reponseMessage.openSuccessMessage(response)
  }

  openNoteDialog(user: User): void {
    this.dialog.open(ReviewAndWriteAdminNotesComponent, {
      data: {id: user.id},
    });
  }
}
