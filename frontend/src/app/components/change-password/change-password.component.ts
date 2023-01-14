import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LoggedUserService} from "../../services/loggedUser/logged-user.service";
import {NewPassword} from "../../model/user/newPassword";
import {UserService} from "../../services/userService/user-sevice.service";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  hideCurrPassword: boolean = true;
  hideNewPassword: boolean = true;
  hideRepeatPassword: boolean = true;

  passwordForm = new FormGroup({
    currPassword: new FormControl('', [Validators.required]),
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
    let currPass = this.passwordForm.controls.currPassword.value;
    let newPass = this.passwordForm.controls.newPassword.value;
    let repPass = this.passwordForm.controls.repeatedPassword.value;

    if (currPass === newPass) {
      this.passwordForm.controls['newPassword'].setErrors({'passwordSame': true})
      return false;
    }
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
    payload.currentPassword = this.passwordForm.controls.currPassword.value;
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
