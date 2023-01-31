import {Component, OnInit} from '@angular/core';
import {NotificationM} from "../../../model/notifications/notification";
import {NotificationService} from "../../../services/notificationService/notification.service";
import {DrivingService} from "../../../services/drivingService/driving.service";
import {UserService} from 'src/app/services/userService/user-sevice.service';
import {AdminRespondOnChanges} from 'src/app/model/message/AdminResponseOnChanges';
import {Driver} from 'src/app/model/driver/driver';
import {Car} from 'src/app/model/car/car';
import {getCarType} from 'src/app/model/car/carType';
import {CarService} from 'src/app/services/carService/car.service';

@Component({
  selector: 'app-notification-review',
  templateUrl: './notification-review.component.html',
  styleUrls: ['./notification-review.component.scss']
})
export class NotificationReviewComponent implements OnInit {

  notifications: NotificationM[];
  carString: string = '';

  constructor (
    private notificationService: NotificationService,
    private drivingService: DrivingService,
    private userService: UserService,
    private carService: CarService
  ) { }

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

  onNoClickChangeData(driverData : string, id: number){
    let email = driverData.split("|")[5];
    this.userService.adminRespondOnChanges(new AdminRespondOnChanges(email, "deny"));
    this.deleteNotification(id);
  }

  onYesClickChangeCarData(carStr: string, id: number){
    let car : Car = this.createCar(carStr);
    this.carService.editCarData(car).subscribe({
      next: (response) => {
        let adminResponse : AdminRespondOnChanges = new AdminRespondOnChanges(car.driverEmail, "accept");
        this.userService.adminRespondOnChanges(adminResponse)
      }
    })
    this.deleteNotification(id);
  }

  onNoClickChangeCarData(carStr: string, id: number){
    let email = carStr.split("|")[11];
    this.userService.adminRespondOnChanges(new AdminRespondOnChanges(email, "deny"));
    this.deleteNotification(id);
  }

  createCar(carStr: string): Car{
    let tokens : string[] = carStr.split("|");
    let car = new Car();
    car.registration = tokens[1];
    car.model = tokens[3];
    car.carType = getCarType[tokens[5].toUpperCase()];
    car.maxPassengers =  Number(tokens[7]);
    car.accommodations = tokens[9];
    car.driverEmail = tokens[11];
    car.id = Number(tokens[13]);
    return car;

  }
}
