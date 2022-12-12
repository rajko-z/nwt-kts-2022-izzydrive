import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-ordering-ride-basic',
  templateUrl: './ordering-ride-basic.component.html',
  styleUrls: ['./ordering-ride-basic.component.scss']
})
export class OrderingRideBasicComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  onSubmit(){
    console.log("Submit");

  }

}
