import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';
import { ErrorHandlerService } from 'src/app/services/errorHandler/error-handler.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss','./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  
  ngOnInit(): void {
  }

  constructor(private router : Router, 
              private userService: UserSeviceService, 
              private errorHandler: ErrorHandlerService,
              private messageTooltip: MatSnackBar ) {

    }


  onSubmit(registerForm: FormGroup): void {
    this.userService.registration(registerForm.value).subscribe(
      ({
        next : (responce) => {
          console.log(responce)
      },
        error: (error )=> {
          this.handleError(error.error, registerForm);
      }
      })
    )
  }

  handleError(errorData : {statusCode: number, message: string, timestamp: Date}, registerForm: FormGroup): void{
    let errorLabel = this.errorHandler.customErrorCode[errorData.statusCode]
    if(errorLabel !== "other"){
      registerForm.controls[errorLabel].setErrors({'incorrect': true})
    }
    else{
      this.openErrorMessage(errorData.message);
    }
  }

  openErrorMessage(message: string): void{
    this.messageTooltip.open(message, 'Close', {
      horizontalPosition: "center",
      verticalPosition: "top",
      panelClass: ['messageTooltip']
    });
  }

  
}
