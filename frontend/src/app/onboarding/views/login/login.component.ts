import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, NgForm, Validators} from '@angular/forms';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss','./login.component.scss'],
  providers: [UserSeviceService]
})
export class LoginComponent implements OnInit {

  hide : boolean = true;
  errorMessage : boolean = false;

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.email]),
    password: new FormControl(''),
  });

  constructor(private http: HttpClient, private userService : UserSeviceService) { }

  ngOnInit(): void {
  }

  getErrorMessage() {
    // if (this.email.hasError('required')) {
    //   return 'You must enter a value';
    // }

    // return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  onSubmit(){
    this.http
    .post(
      "http://localhost:8092/izzydrive/v1/auth/login", 
      this.loginForm.value
    )
    .subscribe({

       next : (responce) => {
        console.log(responce);
        this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["role"]})
        //redirekcija na ulogovanu pocetnu stranicu
      },
      error: (error )=> {
          this.errorMessage = true;
          console.log(error);
          
      }
    })
  }


}
