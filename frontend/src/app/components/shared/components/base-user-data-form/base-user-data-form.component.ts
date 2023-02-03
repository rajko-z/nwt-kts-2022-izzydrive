import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-base-user-data-form',
  templateUrl: './base-user-data-form.component.html',
  styleUrls: ['./base-user-data-form.component.scss']
})
export class BaseUserDataFormComponent implements OnInit {

  hidePassword: boolean = true;
  hideRepeatPassword: boolean = true;
  name_regexp = "^[a-zA-Z]+$";

  isRegistration: boolean = this.userService.getRoleCurrentUserRole() === null; //show pasword if user is not loged in

  registerForm = new FormGroup({
    firstName: new FormControl('', [Validators.required, Validators.pattern(this.name_regexp)]),
    lastName: new FormControl('', [Validators.required, Validators.pattern(this.name_regexp)]),
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('',),
    repeatedPassword: new FormControl(''),
    phoneNumber: new FormControl('', [Validators.required, Validators.pattern("^[+][0-9]{11,12}$")]),
  });


  @Output() register = new EventEmitter<FormGroup>();

  constructor(private userService: UserService) {

  }

  ngOnInit(): void {
    if (this.isRegistration) {
      this.registerForm.controls.password.setValidators([Validators.required, Validators.minLength(8)]);
      this.registerForm.controls.repeatedPassword.setValidators([Validators.required, Validators.minLength(8)]);
    }
  }


  onSubmit(): void {
    this.register.emit(this.registerForm);
  }
}
