import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Driving} from 'src/app/model/driving/driving';
import {Evaluation} from 'src/app/model/evaluation/evaluation';
import {EvaluationService} from 'src/app/services/evaluationService/evaluation.service';
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-evaluation',
  templateUrl: './evaluation.component.html',
  styleUrls: ['./evaluation.component.scss']
})
export class EvaluationComponent {

  grades : number[] = [1,2,3,4,5];
  selectedDriverGrade : number = 0;
  selectedVehicleGrade : number = 0;
  constructor(public dialogRef: MatDialogRef<EvaluationComponent>,
    @Inject(MAT_DIALOG_DATA) public data : Driving,
    private snackbar: MatSnackBar,
    private evaluationService : EvaluationService,
    private responseMessage: ResponseMessageService) { }

  evaluationForm = new FormGroup({
    comment: new FormControl(''),
  });
  onSelectedDriverGrade(grade : number){
    this.selectedDriverGrade = grade;
  }

  onSelectedVehicleGrade(grade : number){
    this.selectedVehicleGrade = grade;
  }

  onSubmit(){
    if(this.selectedDriverGrade === 0 && this.selectedVehicleGrade === 0 && !this.evaluationForm.value.comment){
      this.responseMessage.openSuccessMessage("Must select at least one option");
    }
    else{
      const evaluation : Evaluation = new Evaluation(this.evaluationForm.value.comment,  this.data.id, this.selectedDriverGrade, this.selectedVehicleGrade);
      this.evaluationService.addNewEvaluation(evaluation).subscribe({
        next: (response)=>{
          this.responseMessage.openSuccessMessage(response.text)
          this.data.evaluationAvailable = false;
          this.dialogRef.close()
        },
        error:(erros)=>{
          this.responseMessage.openErrorMessage(erros.error.message);
          this.dialogRef.close()
        }
      })
    }
  }

}
