import {Component} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-driving-history',
  templateUrl: './driving-history.component.html',
  styleUrls: ['./driving-history.component.scss']
})
export class DrivingHistoryComponent {

  constructor(public dialog: MatDialog) { }

}
