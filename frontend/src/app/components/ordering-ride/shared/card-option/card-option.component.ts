import {Component, Input} from '@angular/core';
import {DrivingOption} from "../../../../model/driving/drivingOption";

@Component({
  selector: 'app-card-option',
  templateUrl: './card-option.component.html',
  styleUrls: ['./card-option.component.scss']
})
export class CardOptionComponent {
  @Input() option: DrivingOption;
  @Input() selectedOption: DrivingOption;
  @Input() i: number;


  constructor() {
  }

}
