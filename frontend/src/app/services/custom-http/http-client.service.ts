import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {UserService} from '../userService/user-sevice.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(private http: HttpClient, private userService: UserService) {}

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

  postT<Type>(url, data) {
    return this.http.post<Type>(url, data , {
      headers: this.createHeader(),
    });
  }

  getT<Type>(url) {
    return this.http.get<Type>(url, {
      headers: this.createHeader()
    });
  }

  getWithText(url) {
    return this.http.get(url, {
      headers: this.createHeader(),
      responseType: 'text'
    });
  }

  postWithText(url, data) {
    return this.http.post(url, data, {
      headers: this.createHeader(),
      responseType: 'text'
    });
  }

  put(url, data) {
    return this.http.put(url, data, {
      headers: this.createHeader()
    });
  }

  putT<Type>(url, data) {
    return this.http.put<Type>(url, data , {
      headers: this.createHeader(),
    });
  }
}
