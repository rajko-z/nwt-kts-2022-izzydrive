import {Component} from '@angular/core';
import {UserService} from '../../services/userService/user-sevice.service';
import {Router} from '@angular/router';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  sign_up_clicked: boolean = false;
  sign_in_clicked: boolean = true;

  constructor(private userService: UserService, private router: Router, private _sanitizer: DomSanitizer) {

  }

  isUserLoggedIn(): boolean {
    return this.userService.getCurrentUserToken() ? true : false;
  }

  openHome() {
    this.router.navigateByUrl('/');
  }
}
