import {Component, Input, OnInit} from '@angular/core';
import {PlaceOnMap} from "../../../../model/map/placeOnMap";

@Component({
  selector: 'app-ride-overview',
  templateUrl: './ride-overview.component.html',
  styleUrls: ['./ride-overview.component.scss']
})
export class RideOverviewComponent implements OnInit {
  @Input() start: string;
  @Input() end:string;
  @Input() intermediateStation: PlaceOnMap[];

  constructor() { }

  ngOnInit(): void {
  }

}
