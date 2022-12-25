import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {UserSeviceService} from '../userService/user-sevice.service';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(private http: HttpClient, private userService: UserSeviceService) {}

  public createHeader(){
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
       'Authorization': "Bearer "+ this.userService.getCurrentUserToken()
    });
    return headers;
  }
  get(url) {
    return this.http.get(url, {
      headers: this.createHeader(),
    });
  }

  post(url, data) {
    return this.http.post(url, data, {
      headers: this.createHeader()
    });
  }

  //ovo treba premestiti -konsultovati se sa natasom
  blockUser(id) {
    return this.http.get(environment.apiUrl + `users/block/${id}`, {
      headers: this.createHeader(),responseType: 'text'
    });
  }
  unblockUser(id) {
    return this.http.get(environment.apiUrl + `users/unblock/${id}`, {
      headers: this.createHeader(),responseType: 'text'
    });

  }
}
