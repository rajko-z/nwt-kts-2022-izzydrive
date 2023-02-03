import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl} from "@angular/forms";
import {DrivingService} from "../../../services/drivingService/driving.service";
import {ResponseMessageService} from "../../../services/response-message/response-message.service";
import {DrivingWithLocations} from "../../../model/driving/driving";

@Component({
  selector: 'app-admin-search-ride',
  templateUrl: './admin-search-ride.component.html',
  styleUrls: ['./admin-search-ride.component.scss']
})
export class AdminSearchRideComponent implements OnInit {

  @Output()
  private $drivings = new EventEmitter<DrivingWithLocations[]>();

  searchTerm: FormControl = new FormControl('');

  constructor(
    private drivingService: DrivingService,
    private responseMessage: ResponseMessageService) { }

  ngOnInit(): void {
  }

  onSearch() {
    if (this.searchTerm.value) {
      const term = this.searchTerm.value.trim();
      this.drivingService.getAllCurrentBySearchTerm(term)
        .subscribe({
            next: (drivings) => {
              this.$drivings.emit(drivings);
            },
            error: (error) => {
              this.responseMessage.openErrorMessage(error.error.message)
            }
          }
        );
    } else {
      this.emitAllCurrentDrivings();
    }
  }

  emitAllCurrentDrivings() {
    this.drivingService.getAllCurrent()
      .subscribe({
          next: (drivings) => {
            this.$drivings.emit(drivings);
          },
          error: (error) => {
            this.responseMessage.openErrorMessage(error.error.message)
          }
        }
      );
  }
}
