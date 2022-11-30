import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';

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
                                                          Validators.minLength(13), 
                                                          Validators.maxLength(13)]),
  });


  constructor(private router : Router, private userService: UserSeviceService) {}

  ngOnInit(): void {}

  onSubmit(): void {
    this.userService.registration(this.registerForm.value).subscribe(
      ({
        next : (responce) => {
          console.log(responce)
       
      },
        error: (error )=> {
          
          console.log(error);
          
      }
      })
    )
  }

  
}
