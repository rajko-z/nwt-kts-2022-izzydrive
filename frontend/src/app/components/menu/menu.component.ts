import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getRole, Role } from 'src/app/model/user/role';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import {MatDialog} from "@angular/material/dialog";
import {ChangePasswordComponent} from "../change-password/change-password.component";
import {PayingInfoComponent} from "../paying-info/paying-info.component";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss', ]
})
export class MenuComponent implements OnInit {

  isPassenger : boolean =  getRole[this.userService.getRoleCurrentUserRole()] === Role.ROLE_PASSENGER
  isAdmin: boolean = getRole[this.userService.getRoleCurrentUserRole()] === Role.ROLE_ADMIN
  isDriver: boolean = getRole[this.userService.getRoleCurrentUserRole()] === Role.ROLE_DRIVER

  email: string = this.userService.getCurrentUserEmail();
  name: string = '';

  constructor(private userService: UserService, private router: Router, private matDialog: MatDialog) { }

  ngOnInit(): void {
    this.colapseMenu();
    this.userService.getCurrentUserData().subscribe({
      next: (result) => {
        this.name = result.firstName + " " + result.lastName;
      }
    })
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

  onChangeProfile(){
    this.router.navigateByUrl("/user/edit-profile")
  }

  openChangePasswordDialog() {
    this.matDialog.open(ChangePasswordComponent);
  }

  openPayingInfoDialog() {
    this.matDialog.open(PayingInfoComponent);
  }

  logOut() {
    this.userService.logOut();
  }
}
