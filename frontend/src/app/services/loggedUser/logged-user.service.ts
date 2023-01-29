import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClientService} from "../custom-http/http-client.service";
import {NewPassword} from "../../model/user/newPassword";
import {TextResponse} from "../../model/response/textresponse";
import {User} from "../../model/user/user";
import {UserService} from "../userService/user-sevice.service";
import {DriverService} from "../driverService/driver.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class LoggedUserService {

  constructor(private httpClientService: HttpClientService,
              private userService: UserService,
              private driverService: DriverService,
              private router: Router,
              private snackBar: MatSnackBar) {
  }

  blockUser(id) {
    return this.httpClientService.getWithText(environment.apiUrl + `users/block/${id}`);
  }

  unblockUser(id) {
    return this.httpClientService.getWithText(environment.apiUrl + `users/unblock/${id}`);
  }

  changePassword(payload: NewPassword) {
    return this.httpClientService.postT<TextResponse>(environment.apiUrl + 'users/change-password', payload);
  }

  getAllUsers(){
    return this.httpClientService.getT<User[]>(environment.apiUrl + 'users');
  }

  logOut() {
    if (this.userService.getRoleCurrentUserRole() === 'ROLE_DRIVER') {
      this.driverService.setDriverStatusToInactive()
        .subscribe({
            next: (_) => {
              this.router.navigate(['anon/login']);
              sessionStorage.removeItem('currentUser');
            },
            error: (error) => {
              this.snackBar.open(error.error.message, "ERROR", {
                duration: 5000,
              })
            }
          }
        );
    }
    else {
      this.router.navigate(['anon/login']);
      sessionStorage.removeItem('currentUser');
    }
  }
}
