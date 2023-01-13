import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClientService} from "../custom-http/http-client.service";

@Injectable({
  providedIn: 'root'
})
export class LoggedUserService {

  constructor(private httpClientService: HttpClientService) { }

  blockUser(id) {
    return this.httpClientService.getWithText(environment.apiUrl + `users/block/${id}`);
  }

  unblockUser(id) {
    return this.httpClientService.getWithText(environment.apiUrl + `users/unblock/${id}`);
  }
}
