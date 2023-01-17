import { Component, OnInit , NgZone } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import {
  SocialAuthService,
  FacebookLoginProvider,
  SocialUser
} from '@abacritt/angularx-social-login';
import {CredentialResponse, PromptMomentNotification} from 'google-one-tap';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
import { ChatService } from 'src/app/services/chat/chat.service';
import { Role } from 'src/app/model/user/role';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss','./login.component.scss'],
  providers: [UserService]
})
export class LoginComponent implements OnInit {

  private clientId = environment.clientId

  hide : boolean = true;
  errorMessage : boolean = false;
  errorMessageText: string;
  socialUser!: SocialUser;
  isLoggedin?: boolean = undefined;

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.email]),
    password: new FormControl(''),
  });

  constructor(private userService : UserService,
              private socialAuthService: SocialAuthService,
              private _ngZone: NgZone, 
              private router: Router,
              private responseMessage: ResponseMessageService,
              private chatService : ChatService) { 

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
        this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["user"].role, id: responce["user"].id})
        if (responce["user"].role === Role.ROLE_ADMIN.toString()){
          this.chatService.checkNewMessagesForAdmin();
        }
        this.router.navigateByUrl('/logged')
    },
      error: (error )=> {
          this.responseMessage.openErrorMessage(error.error.message);
    }
  })
  }

  onSubmit(){
   this.userService.regularLogin(this.loginForm.value)
    .subscribe({

        next : (responce) => {
          console.log(responce)
        this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["user"].role, id: responce["user"].id})
        if (responce["user"].role === Role.ROLE_ADMIN.toString()){
          this.chatService.checkNewMessagesForAdmin();
        }
        this.router.navigateByUrl('/driver')
        //   this.router.navigateByUrl('/logged')
      },
        error: (error )=> {
          this.responseMessage.openErrorMessage(error.error.message);
      }
    })
  }

  loginWithFacebook(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID).then(() => {
      this.userService.loginWithFacebook(this.socialUser.authToken)
      .subscribe({

          next : (responce) => {
            console.log(responce);
          this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["user"].role, id: responce["user"].id})
          if (responce["user"].role === Role.ROLE_ADMIN.toString()){
            this.chatService.checkNewMessagesForAdmin();
          }
          this.router.navigateByUrl('/logged')
        },
          error: (error )=> {
            this.responseMessage.openErrorMessage(error.error.message);   

        }
      })
    });

  }

  signOut(): void {
    this.socialAuthService.signOut();
  }


}
