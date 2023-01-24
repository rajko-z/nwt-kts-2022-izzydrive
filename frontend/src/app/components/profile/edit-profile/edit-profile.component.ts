import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import * as _ from 'lodash';
import { environment } from 'src/environments/environment';
import { User } from 'src/app/model/user/user';
import { Router } from '@angular/router';
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {

  name_regexp = "^[a-zA-Z]+$";
  profileImage : SafeResourceUrl;
  imageError: string;
  isImageChanged: boolean = false;
  imageBase64: string;

  editForm: FormGroup = new FormGroup({
            firstName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
            lastName: new FormControl('',[Validators.required, Validators.pattern(this.name_regexp)]),
            email: new FormControl('',[Validators.email, Validators.required]),
            phoneNumber: new FormControl('',[Validators.required, Validators.pattern("^[+][0-9]{11,12}$")]),
  });

  constructor(private userService : UserService,
              private sanitizer: DomSanitizer,
              private router : Router,
              private responseMessage: ResponseMessageService) {

  }

  ngOnInit(): void {
    this.userService.getCurrrentUserDataWithImg().subscribe({
      next: (response) => {
        this.setPlaceholders(response);
      },
      error: (errorRespoonse) => {
        this.responseMessage.openErrorMessage(errorRespoonse);
      }
    }
    )
  }

  setPlaceholders(userData) : void{
    this.profileImage = userData.imageName?  this.sanitizer.bypassSecurityTrustResourceUrl(`data:image/png;base64, ${userData.imageName}`) : null; //proveriti??
    console.log(this.profileImage)
    this.editForm.controls.firstName.setValue(userData.firstName);
    this.editForm.controls.lastName.setValue(userData.lastName);
    this.editForm.controls.email.setValue(userData.email);
    this.editForm.controls.phoneNumber.setValue(userData.phoneNumber);

  }

  fileChangeEvent(fileInput : Event) : boolean{
    this.imageError = null;
    if (fileInput.target["files"] && fileInput.target["files"][0]) {
        const max_size = environment.maxImageSize;
        const allowed_types = ['image/png', 'image/jpeg'];
        const max_height = environment.maxImageHeight;
        const max_width = environment.maxImageWidth;

        if (fileInput.target["files"][0].size > max_size) {
            this.imageError =
                'Maximum size allowed is ' + max_size / 1000 + 'Mb';

            return false;
        }

        if (!_.includes(allowed_types, fileInput.target["files"][0].type)) {
            this.imageError = 'Only Images are allowed ( JPG | PNG )';
            return false;
        }
        const reader = new FileReader();
        reader.onload = (e: Event) => {
            const image = new Image();
            image.src = e.target["result"];
            image.onload = result => {
                const img_height = result.currentTarget['height'];
                const img_width = result.currentTarget['width'];

                if (img_height > max_height && img_width > max_width) {
                    this.imageError =
                        'Maximum dimentions allowed ' +
                        max_height +
                        '*' +
                        max_width +
                        'px';
                    return false;
                } else {
                    this.imageBase64 =  e.target["result"];
                    this.isImageChanged = true;
                    this.profileImage = this.sanitizer.bypassSecurityTrustResourceUrl(this.imageBase64);
                }
            };
        };

        reader.readAsDataURL(fileInput.target["files"][0]);
    }
  }

  onSubmit() : void{
    let user : User = this.editForm.value;
    user.imageName = this.isImageChanged? this.imageBase64.substring(22, ) : null;
    console.log(user);
    this.userService.changeUserData(user).subscribe({
      next : (response) => {
        this.responseMessage.openSuccessMessage(response.text);
        this.router.navigateByUrl('/logged');
      },
      error : (response) => {
        this.responseMessage.openErrorMessage(response.error.message);
      }
    })
  }

}
