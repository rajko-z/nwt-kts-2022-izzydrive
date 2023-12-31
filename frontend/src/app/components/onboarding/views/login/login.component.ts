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
import { EmailInputComponent } from 'src/app/components/profile/email-input/email-input.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../../components/onboarding-header/onboarding-header.component.scss','./login.component.scss'],
  providers: [UserService]
})
export class LoginComponent implements OnInit {

  private clientId = environment.clientId

  hide : boolean = true;
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
              private chatService : ChatService,
              private matDialog: MatDialog
) {

  }

  ngOnInit(): void {
    this.loginForm.controls.email.markAsTouched()
    // @ts-ignore
    if(window["google"]){
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
    }

    this.socialAuthService.authState.subscribe((user) => {
      this.socialUser = user;
      this.isLoggedin = user != null;
    });


  }

  switchHide(){
    this.hide = !this.hide
  }

  async loginWithGoogle(response: CredentialResponse) {
     this.userService.loginWithGoogle(response.credential)
     .subscribe({

      next : (responce) => {
        this.userService.setCurrentUser({email : responce["user"].email, token: responce["token"], role: responce["user"].role, id: responce["user"].id})
        if (responce["user"].role === Role.ROLE_ADMIN.toString()){
          this.chatService.checkNewMessagesForAdmin();
        }
        this.router.navigateByUrl('/logged')
    },
      error: (error )=> {
          this.responseMessage.openErrorMessage("Login failed. You are not register");
    }
  })
  }

  onSubmit() {
    this.userService.regularLogin(this.loginForm.value)
      .subscribe({
        next: (response) => {
          this.userService.setCurrentUser({
            email: response["user"].email,
            token: response["token"],
            role: response["user"].role,
            id: response["user"].id
          })
          if (response["user"].role === Role.ROLE_ADMIN.toString()) {
            this.chatService.checkNewMessagesForAdmin();
          }
          this.router.navigateByUrl('')
        },
        error: (error) => {
          this.responseMessage.openErrorMessage("Login failed. You are not registered");
        }
      })
  }

  loginWithFacebook(): void {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID).then(() => {
      this.userService.loginWithFacebook(this.socialUser.authToken)
      .subscribe({

          next : (responce) => {
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

  onResetPassword(){
    this.matDialog.open(EmailInputComponent, {
      width:'400px',   // Set width to 600px
      height:'350px',  // Set height to 530px
    });
  }
}
