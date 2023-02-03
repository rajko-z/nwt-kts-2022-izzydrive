import {Component, Input, OnInit} from '@angular/core';
import {DrivingOption} from "../../../../model/driving/drivingOption";

@Component({
  selector: 'app-card-option',
  templateUrl: './card-option.component.html',
  styleUrls: ['./card-option.component.scss']
})
export class CardOptionComponent implements OnInit {
  @Input() option: DrivingOption;
  @Input() selectedOption: DrivingOption;
  @Input() i: number;


  constructor() {
  }

  ngOnInit(): void {
  }

}
