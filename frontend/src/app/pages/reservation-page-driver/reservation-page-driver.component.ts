import { Component, OnInit } from '@angular/core';
import {Driving} from "../../model/driving/driving";

@Component({
  selector: 'app-reservation-page-driver',
  templateUrl: './reservation-page-driver.component.html',
  styleUrls: ['./reservation-page-driver.component.scss']
})
export class ReservationPageDriverComponent implements OnInit {
  reservation: Driving;

  constructor() { }

  ngOnInit(): void {
  }

}
