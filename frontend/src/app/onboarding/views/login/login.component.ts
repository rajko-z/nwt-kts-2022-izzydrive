import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, NgForm, Validators} from '@angular/forms';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss','./login.component.scss']
})
export class LoginComponent implements OnInit {

  hide : boolean = true;

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.email]),
    password: new FormControl(''),
  });

  constructor() { }

  ngOnInit(): void {
  }

  getErrorMessage() {
    // if (this.email.hasError('required')) {
    //   return 'You must enter a value';
    // }

    // return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  onSubmit(){
    console.log(this.loginForm);
  }

}
