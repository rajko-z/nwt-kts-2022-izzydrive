import { Component, OnInit } from '@angular/core';
import {Driving} from "../../model/driving/driving";
import {Address} from "../../model/address/address";

@Component({
  selector: 'app-home-page-driver',
  templateUrl: './home-page-driver.component.html',
  styleUrls: ['./home-page-driver.component.scss']
})
export class HomePageDriverComponent implements OnInit {

  currentDriving:Driving;
  futureDriving:Driving;
  start: Address;
  end: Address;
  status: boolean;

  constructor() { }

  ngOnInit(): void {
    this.status = true;
    // @ts-ignore
    this.start = {
      state: "Srbija",
      city: "Novi Sad",
      street : "Branislava Nusica 16"
    }
    // @ts-ignore
    this.end = {
      state: "Srbija",
      city: "Novi Sad",
      street : "Somborska 13"
    }
    // @ts-ignore
    this.currentDriving = {
      id : 1,
      price :  320,
      startDate: new Date(),
      passengers: ["Nika Nikic"],
      start: this.start,
      end: this.end
    }
    // @ts-ignore
    this.futureDriving = {
      id : 2,
      price :  320,
      startDate: new Date(),
      passengers: ["Nika Nikic", "Milojko Maric"],
      start: this.start,
      end: this.end
    }
  }

}
