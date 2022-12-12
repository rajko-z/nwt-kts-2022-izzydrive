import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-ordering-ride-advanced',
  templateUrl: './ordering-ride-advanced.component.html',
  styleUrls: ['./ordering-ride-advanced.component.scss']
})
export class OrderingRideAdvancedComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  onSubmit(){
    console.log("Submit");

  }
}
