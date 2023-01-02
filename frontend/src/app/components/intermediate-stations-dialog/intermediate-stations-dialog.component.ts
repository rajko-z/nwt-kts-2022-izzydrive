import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-intermediate-stations-dialog',
  templateUrl: './intermediate-stations-dialog.component.html',
  styleUrls: ['./intermediate-stations-dialog.component.scss']
})
export class IntermediateStationsDialogComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data, private dialogRef: MatDialogRef<IntermediateStationsDialogComponent>) { }

  ngOnInit(): void {
  }

}
