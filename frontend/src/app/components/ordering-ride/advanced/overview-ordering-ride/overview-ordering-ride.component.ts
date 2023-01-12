import {Component, Input, OnInit} from '@angular/core';
import {DrivingFinderRequest} from "../../../../model/driving/drivingFinderRequest.";
import {DrivingOption} from "../../../../model/driving/drivingOption";
import {HttpClientService} from "../../../../services/custom-http/http-client.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {DrivingService} from "../../../../services/drivingService/driving.service";
import {DrivingRequest} from "../../../../model/driving/driving";
import {Router} from "@angular/router";

@Component({
  selector: 'app-overview-ordering-ride',
  templateUrl: './overview-ordering-ride.component.html',
  styleUrls: ['./overview-ordering-ride.component.scss']
})
export class OverviewOrderingRideComponent implements OnInit {

  @Input() drivingFinderRequest?: DrivingFinderRequest;
  @Input() selectedOption: DrivingOption;

  apiLoading: boolean = false;

  constructor(
    private http: HttpClientService,
    private snackBar: MatSnackBar,
    private drivingService: DrivingService,
    private router: Router
  ) {}

  ngOnInit(): void {
  }

  onSubmit() {
    let payload = new DrivingRequest();
    payload.drivingOption = this.selectedOption;
    payload.drivingFinderRequest = this.drivingFinderRequest;

    console.log(payload);

    this.apiLoading = true;
    this.drivingService.processDrivingRequest(payload)
      .subscribe({
          next: (response) => {
            this.apiLoading = false;
            this.router.navigateByUrl('/payment');
          },
          error: (error) => {
            this.apiLoading = false;
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

}