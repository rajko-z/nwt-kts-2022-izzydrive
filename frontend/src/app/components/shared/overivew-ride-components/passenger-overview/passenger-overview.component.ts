import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-passenger-overview',
  templateUrl: './passenger-overview.component.html',
  styleUrls: ['./passenger-overview.component.scss']
})
export class PassengerOverviewComponent implements OnInit {
  @Input() passengers: string[];
  constructor() { }

  ngOnInit(): void {
  }

}
