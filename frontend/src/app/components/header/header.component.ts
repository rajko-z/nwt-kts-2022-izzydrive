import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/userService/user-sevice.service';
import {Router} from '@angular/router';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  sign_up_clicked: boolean = false;
  sign_in_clicked: boolean = true;

  profilePhotoPath : SafeResourceUrl = '/assets/dark_logo.png'; //IZMENITI DA BUDE PRAVA SLIKA
  constructor(private userService : UserService, private router: Router, private _sanitizer: DomSanitizer) {

  }

  ngOnInit(): void {
   }


  isUserLoggedIn() : boolean{
    return this.userService.getCurrentUserToken()? true : false;
  }

  getPhoto() {
    return this.userService.getProfilePhotoCurrentUser();
  }

  openProfile(): void{
    this.router.navigateByUrl('/profile');
  }

}
