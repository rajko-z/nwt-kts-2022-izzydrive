import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProfilePageData } from 'src/app/model/user/profileData';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-profile-page-user',
  templateUrl: './profile-page-user.component.html',
  styleUrls: ['./profile-page-user.component.scss']
})
export class ProfilePageUserComponent implements OnInit {

  constructor(private userService: UserService, private snackbar : MatSnackBar) { }
  profilePagedata : ProfilePageData;

  ngOnInit(): void {
    
    this.userService.getCurrentUserData().subscribe({
      next : (response) => {
       console.log(response)
       this.profilePagedata = this.userService.getProfilePageDataFromUser(response);
       console.log(this.profilePagedata)
        ;
      },
      error : (error) => {
        this.snackbar.open(error.error.message, "ERROR")
      } 
    })
  }

}
