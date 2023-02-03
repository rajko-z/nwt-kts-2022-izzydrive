import {Component, Input, OnInit} from '@angular/core';
import {DrivingWithLocations} from "../../../../model/driving/driving";

@Component({
  selector: 'app-ride-card',
  templateUrl: './ride-card.component.html',
  styleUrls: ['./ride-card.component.scss']
})
export class RideCardComponent implements OnInit {

  @Input()
  driving: DrivingWithLocations

  constructor() { }

  ngOnInit(): void {
  }

}
