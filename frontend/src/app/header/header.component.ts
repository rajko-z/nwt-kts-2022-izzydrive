import { Component, OnInit } from '@angular/core';
import { UserSeviceService } from '../services/userService/user-sevice.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private userService : UserSeviceService) { }

  ngOnInit(): void {
  }

  isUserLoggedIn() : boolean{
    console.log(this.userService.getTokenFromSessionStorage()? true : false)
    return this.userService.getTokenFromSessionStorage()? true : false;
  }

}
