import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LoggedUserService} from "../../services/loggedUser/logged-user.service";
import {NewPassword} from "../../model/user/newPassword";
import {UserService} from "../../services/userService/user-sevice.service";
import {ResponseMessageService} from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent {

  hideCurrPassword: boolean = true;
  hideNewPassword: boolean = true;
  hideRepeatPassword: boolean = true;

  passwordForm = new FormGroup({
    currPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
    repeatedPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
  });

  constructor(
    private snackBar: MatSnackBar,
    private loggedUserService: LoggedUserService,
    private userService: UserService,
    private messageresponse: ResponseMessageService
  ) {
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
            this.messageresponse.openSuccessMessage(response.text)
          },
          error: (error) => {
            this.messageresponse.openSuccessMessage(error.error.message)
          }
        }
      );
  }
}
