import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import { ResetPasswordComponent } from '../reset-password/reset-password.component';

@Component({
  selector: 'app-email-input',
  templateUrl: './email-input.component.html',
  styleUrls: ['./email-input.component.scss']
})
export class EmailInputComponent implements OnInit {

  emailForm = new FormGroup({
    email: new FormControl('', [Validators.email]),
  });
  constructor(private userService : UserService, 
              private snackbar : MatSnackBar,
              private matDialog: MatDialog) { }

  ngOnInit(): void {
  }

  onSubmit(){
    this.userService.sendResetPasswordEmail(this.emailForm.value.email).subscribe({
      next : (response) => {
        this.snackbar.open("We send you link in email", "OK")
      },
      error : (error) => {
        this.snackbar.open(error.error.message, "ERROR")
      }
    })
  }
}
