import { Component, Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ProfilePageData } from 'src/app/model/user/profileData';
import { User } from 'src/app/model/user/user';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.scss']
})
export class ProfilePageComponent implements OnInit {

  constructor(private router : Router, 
              private userService: UserService,
              private snackbar: MatSnackBar) { }

  @Input() profileData : ProfilePageData;

  ngOnInit(): void {
    console.log(this.profileData)
  }

  onChangeData(){
    console.log(Object.keys(this.profileData.otherAttributes))
    if (Object.keys(this.profileData.otherAttributes).includes("phone number")){
      this.router.navigateByUrl("/user/edit-profile")
    }
    else{
      this.router.navigateByUrl('/driver/edit-car')
    }
  }

}
