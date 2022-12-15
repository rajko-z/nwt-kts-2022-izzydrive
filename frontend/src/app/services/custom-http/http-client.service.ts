import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {UserSeviceService} from '../userService/user-sevice.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(private http: HttpClient, private userService: UserSeviceService) {}

  createHeader(){
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
       'Authorization': "Bearer "+ this.userService.getCurrentUserToken()
    });
    return headers;
  }
  get(url) {   
    return this.http.get(url, {
      headers: this.createHeader()
    });
  }

  post(url, data) {
    return this.http.post(url, data, {
      headers: this.createHeader()
    });
  }
}
