import {Component, Inject} from '@angular/core';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from '@angular/material/snack-bar';
import {NotificationService} from 'src/app/services/notificationService/notification.service';
import {UserService} from 'src/app/services/userService/user-sevice.service';
import {AdminRespondOnChanges} from 'src/app/model/message/AdminResponseOnChanges'
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-driver-change-info',
  templateUrl: './driver-change-info.component.html',
  styleUrls: ['./driver-change-info.component.scss']
})
export class DriverChangeInfoComponent {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
  private userService: UserService,
  private snackbar: MatSnackBar,
  private notificationService: NotificationService,
  private responseMessage: ResponseMessageService) { }

  onYesClick(){
    this.userService.changeUserData(this.data.message.driverData).subscribe({
      next: (response) => {
        let adminResponse : AdminRespondOnChanges = new AdminRespondOnChanges(this.data.message.driverData.email, "accept");
        this.userService.adminRespondOnChanges(adminResponse)
        this.responseMessage.openSuccessMessage(response.text)
      },
      error: (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
    this.notificationService.deleteNotification(this.data.message.id).subscribe((res) => {
    })
  }

  onNoClick(){
    this.data.preClose();
    let adminResponse : AdminRespondOnChanges = new AdminRespondOnChanges(this.data.message.driverData.email, "reject");
    this.userService.adminRespondOnChanges(adminResponse);
    this.notificationService.deleteNotification(this.data.message.id).subscribe((res) => {
    })
  }
}
