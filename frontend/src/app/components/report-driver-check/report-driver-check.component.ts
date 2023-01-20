import {Component, Inject, OnInit} from '@angular/core';
import {MAT_SNACK_BAR_DATA} from "@angular/material/snack-bar";

@Component({
  selector: 'app-report-driver-check',
  templateUrl: './report-driver-check.component.html',
  styleUrls: ['./report-driver-check.component.scss']
})
export class ReportDriverCheckComponent implements OnInit {

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data) { }

  ngOnInit(): void {
  }

  yesClick() {
    //send notification to admin
    //desable or delete button report
    this.data.preClose();
  }

  noClick() {
    this.data.preClose();
  }
}
