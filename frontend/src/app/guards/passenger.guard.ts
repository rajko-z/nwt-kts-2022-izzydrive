import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {UserService} from "../services/userService/user-sevice.service";

@Injectable({
  providedIn: 'root'
})
export class PassengerGuard implements CanActivate {

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!this.userService.isUserLoggedIn()) {
      this.router.navigate(['/anon']);
      return false;
    }
    const role = this.userService.getRoleCurrentUserRole();

    if (role === "ROLE_PASSENGER") {
      return true;
    } else if (role === "ROLE_ADMIN") {
      this.router.navigate(['/admin']);
    } else if (role === "ROLE_DRIVER") {
      this.router.navigate(['/driver']);
    }
    return false;
  }

}
