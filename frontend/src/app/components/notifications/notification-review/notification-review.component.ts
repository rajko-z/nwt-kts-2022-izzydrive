import {Component, OnInit} from '@angular/core';
import {NotificationM} from "../../../model/notifications/notification";
import {NotificationService} from "../../../services/notificationService/notification.service";
import {DrivingService} from "../../../services/drivingService/driving.service";
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { AdminRespondOnChanges } from 'src/app/model/message/AdminResponseOnChanges';
import { Driver } from 'src/app/model/driver/driver';

@Component({
  selector: 'app-notification-review',
  templateUrl: './notification-review.component.html',
  styleUrls: ['./notification-review.component.scss']
})
export class NotificationReviewComponent implements OnInit {

  notifications: NotificationM[];

  constructor(private notificationService: NotificationService, 
    private drivingService: DrivingService,
    private userService: UserService) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  loadData() {
    this.notificationService.findAll().subscribe((res) => {
      this.notifications = res as NotificationM[];
      console.log(this.notifications);
    });
  }

  deleteNotification(id: number) {
    this.notificationService.deleteNotification(id).subscribe((res) => {
      this.loadData();
    })
  }

  onYesClick(drivingId: number, id: number) {

  }

  onNoClick(drivingId: number, id: number) {
    this.drivingService.deleteDriving(drivingId).subscribe((res) => {

    });
    this.deleteNotification(id);
  }

  onYesClickChangeData(driverData : String, id: number){
    let driver : Driver = this.crateDriver(driverData);
    this.userService.changeUserData(driver).subscribe({
      next: (response) => {
        let adminResponse : AdminRespondOnChanges = new AdminRespondOnChanges(driver.email, "accept");
        this.userService.adminRespondOnChanges(adminResponse)
      }
    })
    this.deleteNotification(id);
  }
  crateDriver(driverData: String): Driver {
    let tokens : string[] = driverData.split("|");
    let driver = new Driver();
    driver.firstName = tokens[1];
    driver.lastName = tokens[3];
    driver.email = tokens[5];
    driver.phoneNumber = tokens[7];
    console.log(driver);
    return driver;
  }

  onNoClickChangeData(email : string, id: number){
    this.userService.adminRespondOnChanges(new AdminRespondOnChanges(email, "deny"));
    this.deleteNotification(id);
  }
}
