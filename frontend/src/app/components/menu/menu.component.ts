import { Component, OnInit } from '@angular/core';
import { getRole, Role } from 'src/app/model/user/role';
import { UserService } from 'src/app/services/userService/user-sevice.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss', ]
})
export class MenuComponent implements OnInit {

  isPassenger : boolean =  getRole[this.userService.getRoleCurrentUserRole()] === Role.ROLE_PASSENGER
  isAdmin: boolean = getRole[this.userService.getRoleCurrentUserRole()] === Role.ROLE_ADMIN
  isDriver: boolean = getRole[this.userService.getRoleCurrentUserRole()] === Role.ROLE_DRIVER

  constructor(private userService: UserService) { }

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
