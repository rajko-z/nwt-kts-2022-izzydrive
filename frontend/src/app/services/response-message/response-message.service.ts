import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ResponseMessageService {

  constructor(private messageTooltip: MatSnackBar){

  }

  public openSuccessMessage(message: string): void{
    this.messageTooltip.open(message, 'OK', {
    horizontalPosition: "center",
    duration: 4000,
    verticalPosition: "top",
    });
}

public openErrorMessage(message: string): void{
  this.messageTooltip.open(message, 'ERROR', {
  horizontalPosition: "center",
  verticalPosition: "top",
  duration: 4000,
  panelClass: ['red-snackbar']
  });
}
}
