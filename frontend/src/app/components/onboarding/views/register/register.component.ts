import {Component, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from 'src/app/services/userService/user-sevice.service';
import {ErrorHandlerService} from 'src/app/services/errorHandler/error-handler.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss', './register.component.scss'],
})
export class RegisterComponent implements OnInit {

  ngOnInit(): void {
  }

  constructor(private router: Router,
              private userService: UserService,
              private errorHandler: ErrorHandlerService,
              private responskeMessage: ResponseMessageService) {

  }

  onSubmit(registerForm: FormGroup): void {
    this.userService.registration(registerForm.value).subscribe(
      ({
        next: _ => {
          this.responskeMessage.openSuccessMessage("We sent you an email to verify your account")
          this.router.navigateByUrl("/anon/login")
        },
        error: (error) => {
          this.handleError(error.error, registerForm);
        }
      })
    )
  }

  handleError(errorData: { statusCode: number, message: string, timestamp: Date, errorField: number }, registerForm: FormGroup): void {
    const errorLabel = this.errorHandler.customErrorCode[errorData.errorField]
    this.openErrorMessage(errorData.message);

  }

  openErrorMessage(message: string): void {
    this.responskeMessage.openErrorMessage(message)
  }
}
