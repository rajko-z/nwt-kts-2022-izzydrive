import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { NewPassword } from 'src/app/model/user/newPassword';
import { ResetPassword } from 'src/app/model/user/resetPassword';
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
  isValidToken: boolean = false;
  resetpasswordToke : string;

  passwordForm = new FormGroup({
    newPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
    repeatedPassword: new FormControl('',[Validators.required,Validators.minLength(8)]),
  });

  constructor(
    private snackBar: MatSnackBar,
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    let urlTokens : string[] = window.location.href.split("/")
    this.resetpasswordToke = urlTokens[urlTokens.length - 1];
    this.userService.verifyResetPasswordToken(this.resetpasswordToke).subscribe({
      next: () => {
        this.isValidToken = true;
      },
      error : (error) => {
        console.log(error)
        this.snackBar.open(error.error.message, "ERROR");
      }
    })
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
    let resetPassword : ResetPassword = new ResetPassword;
    resetPassword.password = this.passwordForm.value.newPassword;
    resetPassword.repeatedPassword = this.passwordForm.value.repeatedPassword;
    resetPassword.token = this.resetpasswordToke;
    this.userService.resetPassword(resetPassword).subscribe({
      next : (response) => {
        this.snackBar.open(response.text, "OK")
        this.router.navigateByUrl('/anon/login')
      },
      error : (error) => {
        this.snackBar.open(error.error.message, "ERROR")
      }
    })
  }

}
