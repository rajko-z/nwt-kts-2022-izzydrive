import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClientService} from "../custom-http/http-client.service";
import {NewPassword} from "../../model/user/newPassword";
import {TextResponse} from "../../model/response/textresponse";
import {User} from "../../model/user/user";

@Injectable({
  providedIn: 'root'
})
export class LoggedUserService {

  constructor(private httpClientService: HttpClientService) {
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
  checkUserHasAccount(userEmail: string) {
    return this.httpClientService.getWithText(environment.apiUrl);
  }
  getAllUsers(){
    return this.httpClientService.getT<User[]>(environment.apiUrl + 'users');
  }
}
