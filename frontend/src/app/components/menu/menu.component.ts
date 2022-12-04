import { Component, OnInit } from '@angular/core';
import { UserSeviceService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss', ]
})
export class MenuComponent implements OnInit {

  isPassenger : boolean =  false; //this.userService.getRoleCurrentUserRole() === "passenger"; DRUGACIJE

  isAdmin: boolean = false;  //this.userService.getRoleCurrentUserRole() === "admin";

  isDriver: boolean = true; //DODATI MENI ZA VOZACA

  constructor(private userService: UserSeviceService) { }

  ngOnInit(): void {
   
  }

  colapseMenu(){
    const menuContainer = document.getElementById('menuDiv') as HTMLInputElement;
    menuContainer.style.display = "none";
    const openMenuButton = document.getElementById('collapsedMenu') as HTMLInputElement;
    openMenuButton.style.display = "flex";
  }

  openMenu(){
    const menuContainer = document.getElementById('menuDiv') as HTMLInputElement;
    menuContainer.style.display = "flex";
    const openMenuButton = document.getElementById('collapsedMenu') as HTMLInputElement;
    openMenuButton.style.display = "none";
  }

}
