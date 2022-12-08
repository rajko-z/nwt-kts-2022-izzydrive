import { Component, OnInit } from '@angular/core';
import { UserSeviceService } from '../../services/userService/user-sevice.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private userService : UserSeviceService,
    private router: Router) { 

    }

  ngOnInit(): void {
  }

  isUserLoggedIn() : boolean{
    return this.userService.getCurrentUserToken()? true : false;
  }

  openProfile(): void{
    this.router.navigateByUrl('/profile');
  }

}
