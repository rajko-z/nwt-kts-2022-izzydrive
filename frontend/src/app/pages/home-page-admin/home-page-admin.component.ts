import {Component, OnInit} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {DrivingWithLocations} from "../../model/driving/driving";
import {DrivingService} from "../../services/drivingService/driving.service";
import {FormControl} from "@angular/forms";
import {MapService} from "../../services/mapService/map.service";

@Component({
  selector: 'app-home-page-admin',
  templateUrl: './home-page-admin.component.html',
  styleUrls: ['./home-page-admin.component.scss']
})
export class HomePageAdminComponent implements OnInit {

  drivings?: DrivingWithLocations[]

  selectedDrivingId: number;

  searchTerm: FormControl = new FormControl('');

  constructor(
    private drivingService: DrivingService,
    private snackBar: MatSnackBar,
    private mapService: MapService) { }

  ngOnInit(): void {
    this.loadAllCurrentDrivings();
  }

  loadAllCurrentDrivings() {
    this.drivingService.getAllCurrent()
      .subscribe({
          next: (drivings) => {
            this.drivings = drivings;
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

  onSearch() {
    if (this.searchTerm.value) {
      const term = this.searchTerm.value.trim();
      this.drivingService.getAllCurrentBySearchTerm(term)
        .subscribe({
            next: (drivings) => {
              this.drivings = drivings;
            },
            error: (error) => {
              this.snackBar.open(error.error.message, "ERROR", {
                duration: 5000,
              })
            }
          }
        );
    } else {
      this.loadAllCurrentDrivings();
    }
  }

  showDrivingOnMap(id: number) {
    if (id === this.selectedDrivingId) {
      return;
    }

    this.selectedDrivingId = id;
    this.drivingService.findDrivingWithLocationsById(id)
      .subscribe({
          next: (driving) => {
            this.mapService.resetEverythingOnMap();
            this.mapService.addAllFromDriving(driving);
          },
          error: (_) => {
            this.snackBar.open("Can't find this driving. It is possible that this driving is canceled, please refresh page to load new current drivings", "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

}
