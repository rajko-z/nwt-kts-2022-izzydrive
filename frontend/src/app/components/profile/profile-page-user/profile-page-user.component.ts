import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProfilePageData } from 'src/app/model/user/profileData';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-profile-page-user',
  templateUrl: './profile-page-user.component.html',
  styleUrls: ['./profile-page-user.component.scss']
})
export class ProfilePageUserComponent implements OnInit {

  constructor(private userService: UserService, private responseMessage : ResponseMessageService) { }
  profilePagedata : ProfilePageData;
  isUserLoaded: boolean = false;

  ngOnInit(): void {
    
    this.userService.getCurrrentUserDataWithImg().subscribe({
      next : (response) => {
       this.profilePagedata = this.userService.getProfilePageDataFromUser(response);
       this.isUserLoaded = true;
        ;
      },
      error : (error) => {
        this.responseMessage.openErrorMessage(error.error.message)
      } 
    })
  }

}
