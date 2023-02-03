import {Component, Input, OnInit} from '@angular/core';
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {DomSanitizer, SafeResourceUrl} from "@angular/platform-browser";
import {UserService} from "../../../../services/userService/user-sevice.service";
import {ResponseMessageService} from "../../../../services/response-message/response-message.service";
import {DrivingState, DrivingWithLocations} from "../../../../model/driving/driving";

@Component({
  selector: 'app-driver-overview',
  templateUrl: './driver-overview.component.html',
  styleUrls: ['./driver-overview.component.scss']
})
export class DriverOverviewComponent implements OnInit {
  @Input() option: DrivingOption | DrivingWithLocations;

  driverProfilePhoto: SafeResourceUrl;

  constructor(private _sanitizer: DomSanitizer,private userService: UserService, private responseMessage: ResponseMessageService) { }

  ngOnInit(): void {
    this.setDriverProfilePhoto();
  }

  setDriverProfilePhoto(){
    this.userService.getUserDataWithImage(this.option.driver.email).subscribe(
      {
        next: (response) => {
          this.driverProfilePhoto =  response.imageName?  this._sanitizer.bypassSecurityTrustResourceUrl(`data:image/png;base64, ${response.imageName}`) : null;
        },
        error: (error) => {
          this.responseMessage.openErrorMessage(error.error.message)
        }
      }
    )
  }

}
