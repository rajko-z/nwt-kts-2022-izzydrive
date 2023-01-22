import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NewPassword } from 'src/app/model/user/newPassword';
import { LoggedUserService } from 'src/app/services/loggedUser/logged-user.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  hideNewPassword: boolean = true;
  hideRepeatPassword: boolean = true;

  passwordForm = new FormGroup({
    newPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
    repeatedPassword: new FormControl('',[Validators.required,Validators.minLength(8)]),
  });

  constructor(
    private snackBar: MatSnackBar,
    private loggedUserService: LoggedUserService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  validate(): boolean {
    let newPass = this.passwordForm.controls.newPassword.value;
    let repPass = this.passwordForm.controls.repeatedPassword.value;

    if (newPass !== repPass) {
      this.passwordForm.controls['repeatedPassword'].setErrors({'noMatch': true})
      return false;
    }
    return true;
  }

  onSubmit() {
    if (!this.validate()) {
      return;
    }

    const payload: NewPassword = new NewPassword();
    payload.newPassword = this.passwordForm.controls.newPassword.value;
    payload.email = this.userService.getCurrentUserEmail();

    this.loggedUserService.changePassword(payload)
      .subscribe({
          next: (response) => {
            this.snackBar.open(response.text, "OK", {
              duration: 5000,
            })
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

}
