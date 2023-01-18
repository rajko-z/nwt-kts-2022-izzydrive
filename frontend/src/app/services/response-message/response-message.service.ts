import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ResponseMessageService {

  constructor(private messageTooltip: MatSnackBar){

  }

  public openSuccessMessage(message: string): void{
      this.messageTooltip.open(message, 'Close', {
      horizontalPosition: "center",
      verticalPosition: "top",
      duration: 3000,
      panelClass: ['green-snackbar']
      });
  }

  public openErrorMessage(message: string): void{
    this.messageTooltip.open(message, 'Close', {
    horizontalPosition: "center",
    verticalPosition: "top",
    duration: 3000,
    panelClass: ['red-snackbar']
    });
}
}
