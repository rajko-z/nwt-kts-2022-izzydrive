import { Component, Inject, OnInit } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBar } from '@angular/material/snack-bar';
import { AdminRespondOnChanges } from 'src/app/model/message/AdminResponseOnChanges';
import { CarService } from 'src/app/services/carService/car.service';
import { NotificationService } from 'src/app/services/notificationService/notification.service';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-car-change-info',
  templateUrl: './car-change-info.component.html',
  styleUrls: ['./car-change-info.component.scss']
})
export class CarChangeInfoComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data,
  private userService: UserService,
  private snackbar: MatSnackBar,
  private notificationService: NotificationService,
  private carService: CarService,
  private responseMessage:ResponseMessageService) { }

  ngOnInit(): void {
  }

  onYesClick(){
    this.carService.editCarData(this.data.message.carData).subscribe({
      next: (response) => {
        let adminResponse : AdminRespondOnChanges = new AdminRespondOnChanges(this.data.message.carData.driverEmail, "accept");
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
    let adminResponse : AdminRespondOnChanges = new AdminRespondOnChanges(this.data.message.carData.driverEmail, "reject");
    this.userService.adminRespondOnChanges(adminResponse);
    this.notificationService.deleteNotification(this.data.message.id).subscribe((res) => {
    })
  }
}
