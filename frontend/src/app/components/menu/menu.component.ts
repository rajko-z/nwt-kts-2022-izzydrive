import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getRole, Role } from 'src/app/model/user/role';
import { UserService } from 'src/app/services/userService/user-sevice.service';
import {MatDialog} from "@angular/material/dialog";
import {ChangePasswordComponent} from "../change-password/change-password.component";
import {PayingInfoComponent} from "../paying-info/paying-info.component";
import {LoggedUserService} from "../../services/loggedUser/logged-user.service";
import { ChatService } from 'src/app/services/chat/chat.service';

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

  constructor(
    private userService: UserService,
    private router: Router,
    private matDialog: MatDialog,
    private userLoggedService: LoggedUserService,
    private chatService: ChatService
  ) { }

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
    this.chatService.closeAllAdminChat()
    this.router.navigateByUrl("/user/edit-profile")
  }

  openChangePasswordDialog() {
    this.chatService.closeAllAdminChat()
    this.matDialog.open(ChangePasswordComponent);
  }

  openPayingInfoDialog() {
    this.chatService.closeAllAdminChat()
    this.matDialog.open(PayingInfoComponent);
  }

  logOut() {
    this.userLoggedService.logOut();
  }

  openNotificationReview() {
    this.chatService.closeAllAdminChat()
    this.router.navigateByUrl("/user/notifications");
  }

  openProfilePage(){
    this.chatService.closeAllAdminChat()
    this.router.navigateByUrl("/user/profile-page")
  }

  onReports(){
    this.chatService.closeAllAdminChat()
    this.router.navigateByUrl("/user/reports")
  }
}
