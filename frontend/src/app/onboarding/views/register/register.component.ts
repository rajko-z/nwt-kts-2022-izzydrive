import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss','./register.component.scss'],
})
export class RegisterComponent implements OnInit {

  hide: boolean = true
  //phone_number_regexp: string = "^(\\+\\d{1,2}\\s)?\(?\\d{3}\)?[\\s.-]\\d{3}[\s.-]\\d{4}$";
  name_regexp = "^[a-zA-Z]+$";

  registerForm = new FormGroup({
    firstName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
    lastName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
    email: new FormControl('', [Validators.email, Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)]),
    repeatedPassword: new FormControl('',[Validators.required,Validators.minLength(8)]),
    phoneNumber: new FormControl('',[Validators.required, Validators.pattern("^[+][0-9]*$"),
                                                          Validators.minLength(10), 
                                                          Validators.maxLength(10)]),
  });


  constructor(private router : Router) {}

  ngOnInit(): void {}

  onSubmit(): void {
    console.log(this.registerForm)
  }

  
}
