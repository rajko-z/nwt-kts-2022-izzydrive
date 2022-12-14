import { HttpClient } from '@angular/common/http';
import { Component, OnInit , NgZone } from '@angular/core';
import {FormControl, FormGroup, Validators, FormBuilder} from '@angular/forms';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';
import {
  SocialAuthService,
  FacebookLoginProvider,
  SocialUser
} from '@abacritt/angularx-social-login';
import {CredentialResponse, PromptMomentNotification} from 'google-one-tap';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss','./login.component.scss'],
  providers: [UserSeviceService]
})
export class LoginComponent implements OnInit {

  private clientId = environment.clientId

  hide : boolean = true;
  errorMessage : boolean = false;
  socialUser!: SocialUser;
  isLoggedin?: boolean = undefined;

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.email]),
    password: new FormControl(''),
  });

  constructor(private formBuilder: FormBuilder, 
              private http: HttpClient, 
              private userService : UserSeviceService, 
              private socialAuthService: SocialAuthService,
              private _ngZone: NgZone) { 

  }

  ngOnInit(): void {
    
    window["onGoogleLibraryLoad"] = () => {
      // @ts-ignore
      google.accounts.id.initialize({
        client_id: this.clientId,
        callback: this.loginWithGoogle.bind(this),
        auto_select: false,
        cancel_on_tap_outside: true
      });
      // @ts-ignore
      google.accounts.id.renderButton(
      // @ts-ignore
      document.getElementById("google_button_div"),
        { theme: "outline", size: "large"} 
      );
      // @ts-ignore
      google.accounts.id.prompt((notification: PromptMomentNotification) => {});
    };
    
    this.socialAuthService.authState.subscribe((user) => {
      this.socialUser = user;
      this.isLoggedin = user != null;
    });


  }

  async loginWithGoogle(response: CredentialResponse) {
     this.userService.loginWithGoogle(response.credential)
     .subscribe({

      next : (responce) => {
        console.log(responce);
        this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["role"], id: responce["user"].id})
      //redirekcija na ulogovanu pocetnu stranicu
    },
      error: (error )=> {
        console.log(error);
        
    }
  })
  }

 

  onSubmit(){
   this.userService.regularLogin(this.loginForm.value)
    .subscribe({

        next : (responce) => {
          console.log(responce)
        this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["role"], id: responce["user"].id})
        //redirekcija na ulogovanu pocetnu stranicu
      },
        error: (error )=> {
          this.errorMessage = true;
          console.log(error);
          
      }
    })
  }

  loginWithFacebook(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID).then(() => {
      this.userService.loginWithFacebook(this.socialUser.authToken)
      .subscribe({

          next : (responce) => {
            console.log(responce);
          this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["role"], id: responce["user"].id})
          //redirekcija na ulogovanu pocetnu stranicu
        },
          error: (error )=> {
            this.errorMessage = true;
            console.log(error);   
        }
      })
    });
    
  }

  signOut(): void {
    this.socialAuthService.signOut();
  }


}
