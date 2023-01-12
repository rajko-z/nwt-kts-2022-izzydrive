import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {FormControl, FormGroup} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-ordering-ride-basic',
  templateUrl: './ordering-ride-basic.component.html',
  styleUrls: ['./ordering-ride-basic.component.scss']
})
export class OrderingRideBasicComponent implements OnInit {

  routeForm = new FormGroup({
    startLocation: new FormControl(''),
    endLocation: new FormControl('')
  })
  message;

  constructor(public dialog: MatDialog, public snackBar: MatSnackBar) {
  }

  ngOnInit() {}

  onSubmit() {
  }

}
