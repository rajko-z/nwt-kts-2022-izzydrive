import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.scss']
})
export class EvaluationComponent implements OnInit {

  grades : number[] = [1,2,3,4,5];
  selectedDriverGrade : number = 0;
  selectedVehicleGrade : number = 0;
  constructor(@Inject(MAT_DIALOG_DATA) public data) { }

  ngOnInit(): void {
  }

  onSelectedDriverGrade(grade : number){
    this.selectedDriverGrade = grade;
  }

  onSelectedVehicleGrade(grade : number){
    this.selectedVehicleGrade = grade;
  }

}
