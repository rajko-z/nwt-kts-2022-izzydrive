import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {

  name_regexp = "^[a-zA-Z]+$";
  placeholders = {"firstName": '',
                  "lastName": '',
                  "email": '',
                  "phoneNumber": ''}

  editForm: FormGroup = new FormGroup({ firstName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
  lastName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
  email: new FormControl('', [Validators.email, Validators.required]),
  phoneNumber: new FormControl('',[Validators.required, Validators.pattern("^[+][0-9]*$"),
                                                        Validators.minLength(13), 
                                                        Validators.maxLength(13)]),
    
  });

  constructor(private userService : UserSeviceService) { }

  ngOnInit(): void {
    this.userService.getCurrrentUserDataWithImg().subscribe({
      next: (response) => {
        this.setPlaceholders(response);
      },
      error: (errorRespoonse) => {
        console.log(errorRespoonse);
      }
    }  
    )
  }

  setPlaceholders(userData) : void{
    this.placeholders.firstName = userData.firstName;
    this.placeholders.lastName = userData.lastName;
    this.placeholders.email = userData.email;
    this.placeholders.phoneNumber = userData.phoneNumber;

  }

}
