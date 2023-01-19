import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Driving } from 'src/app/model/driving/driving';
import { Evaluation } from 'src/app/model/evaluation/evaluation';
import { EvaluationService } from 'src/app/services/evaluationService/evaluation.service';

@Component({
  selector: 'app-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.scss']
})
export class EvaluationComponent implements OnInit {

  grades : number[] = [1,2,3,4,5];
  selectedDriverGrade : number = 0;
  selectedVehicleGrade : number = 0;
  constructor(public dialogRef: MatDialogRef<EvaluationComponent>,
    @Inject(MAT_DIALOG_DATA) public data : Driving, private snackbar: MatSnackBar, private evaluationService : EvaluationService) { }

  evaluationForm = new FormGroup({
    comment: new FormControl(''),
  });

  ngOnInit(): void {
  }

  onSelectedDriverGrade(grade : number){
    this.selectedDriverGrade = grade;
  }

  onSelectedVehicleGrade(grade : number){
    this.selectedVehicleGrade = grade;
  }

  onSubmit(){
    if(this.selectedDriverGrade === 0 && this.selectedVehicleGrade === 0 && !this.evaluationForm.value.comment){
      this.snackbar.open("Must select at least one option", "OK");
    }
    else{
      let evaluation : Evaluation = new Evaluation(this.evaluationForm.value.comment,  this.data.id, this.selectedDriverGrade, this.selectedVehicleGrade);
      this.evaluationService.addNewEvaluation(evaluation).subscribe({
        next: (response)=>{
          this.snackbar.open(response.text, "OK")
          this.data.evaluationAvailable = false;
          this.dialogRef.close()
        },
        error:(erros)=>{
          this.snackbar.open(erros.error.message);
          this.dialogRef.close()
        }
      })
    }
  }

}
