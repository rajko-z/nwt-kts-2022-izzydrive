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

  hidePassword: boolean = true;
  hideRepeatPassword: boolean = true
  //phone_number_regexp: string = "^(\\+\\d{1,2}\\s)?\(?\\d{3}\)?[\\s.-]\\d{3}[\s.-]\\d{4}$";
  name_regexp = "^[a-zA-Z]+$";

  registerForm = new FormGroup({
    firstName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
    lastName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)]),
    repeatedPassword: new FormControl('',[Validators.required,Validators.minLength(8)]),
    phoneNumber: new FormControl('',[Validators.required, Validators.pattern("^[+][0-9]*$"),
                                                          Validators.minLength(13),
                                                          Validators.maxLength(13)]),
  });

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

  handleError(errorData : {statusCode: number, message: string, timestamp: Date, errorField: number}, registerForm: FormGroup): void{
    let errorLabel = this.errorHandler.customErrorCode[errorData.errorField]
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
