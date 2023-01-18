import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-home-page-logged',
  templateUrl: './home-page-logged.component.html',
  styleUrls: ['./home-page-logged.component.scss']
})
export class HomePageLoggedComponent implements OnInit {

  reservation: boolean;

  constructor(private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.reservation = data.reservation;
      console.log(this.reservation);
    })
  }

}
