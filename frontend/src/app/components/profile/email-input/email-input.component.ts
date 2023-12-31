import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ResetPasswordComponent } from '../reset-password/reset-password.component';

@Component({
  selector: 'app-email-input',
  templateUrl: './email-input.component.html',
  styleUrls: ['./email-input.component.scss']
})
export class EmailInputComponent {

  emailForm = new FormGroup({
    email: new FormControl('', [Validators.email]),
  });
  constructor(private userService : UserService,
              private responseMessage : ResponseMessageService,) { }
  onSubmit(){
    this.userService.sendResetPasswordEmail(this.emailForm.value.email).subscribe({
      next : (response) => {
        this.responseMessage.openSuccessMessage("We send you link in email")
      },
      error : (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      }
    })
  }
}
