import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DrivingWithLocations} from "../../../model/driving/driving";

@Component({
  selector: 'app-ride-card-list',
  templateUrl: './ride-card-list.component.html',
  styleUrls: ['./ride-card-list.component.scss']
})
export class RideCardListComponent implements OnInit {

  @Input()
  drivings?: DrivingWithLocations[]

  @Output()
  $drivingId = new EventEmitter<number>();


  selectedDrivingId: number;

  constructor() { }

  ngOnInit(): void {
  }

  emitDrivingId(id: number): void {
    if (this.selectedDrivingId === id) {
      return;
    }
    this.selectedDrivingId = id;
    this.$drivingId.emit(id);
  }
}
