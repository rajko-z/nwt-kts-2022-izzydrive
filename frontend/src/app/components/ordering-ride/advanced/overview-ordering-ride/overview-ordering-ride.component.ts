import {Component, Input} from '@angular/core';
import {DrivingFinderRequest} from "../../../../model/driving/drivingFinderRequest.";
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {HttpClientService} from "../../../../services/custom-http/http-client.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DrivingService} from "../../../../services/drivingService/driving.service";
import {DrivingRequest} from "../../../../model/driving/driving";
import {Router} from "@angular/router";
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-overview-ordering-ride',
  templateUrl: './overview-ordering-ride.component.html',
  styleUrls: ['./overview-ordering-ride.component.scss']
})
export class OverviewOrderingRideComponent {

  @Input() drivingFinderRequest?: DrivingFinderRequest;
  @Input() selectedOption: DrivingOption;

  apiLoading: boolean = false;

  constructor(
    private http: HttpClientService,
    private snackBar: MatSnackBar,
    private drivingService: DrivingService,
    private router: Router,
    private responseMessage: ResponseMessageService
  ) {
  }

  onSubmit() {
    let payload = new DrivingRequest();
    this.setNewOrderedIntermediateLocationsIfNeeded();
    payload.drivingOption = this.selectedOption;
    payload.drivingFinderRequest = this.drivingFinderRequest;

    this.apiLoading = true;
    this.drivingService.processDrivingRequest(payload)
      .subscribe({
          next: (response) => {
            this.apiLoading = false;
            this.router.navigateByUrl('passenger/payment');
          },
          error: (error) => {
            this.apiLoading = false;
            this.responseMessage.openErrorMessage(error.error.message)
          }
        }
      );
  }

  onBookRide() {
    let payload = new DrivingRequest();
    payload.drivingOption = this.selectedOption;
    payload.drivingFinderRequest = this.drivingFinderRequest;

    this.apiLoading = true;
    this.drivingService.createReservation(payload)
      .subscribe({
          next: (response) => {
            this.apiLoading = false;
            this.router.navigateByUrl('/passenger');
          },
          error: (error) => {
            this.apiLoading = false;
            this.responseMessage.openErrorMessage(error.error.message)
          }
        }
      );
  }

  setNewOrderedIntermediateLocationsIfNeeded() {
    if (this.selectedOption.startToEndPath.reorderedIntermediate && this.drivingFinderRequest.intermediateLocations.length > 1) {
      this.drivingFinderRequest.intermediateLocations = this.selectedOption.startToEndPath.reorderedIntermediate;
    }
  }
}
