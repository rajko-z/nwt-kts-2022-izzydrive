import { HttpClient } from '@angular/common/http';
import { Token } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginResponse } from 'src/app/model/response/loginResponse';
import { User } from 'src/app/model/user/user';
import { FormControl } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class UserSeviceService {

  constructor(private http: HttpClient) { }

  setCurrentUser(userData : {email: string, token: string, role: string}){
    sessionStorage.setItem('currentUser', JSON.stringify({ token: userData.token, username: userData.email, role: userData.role }));
  }

  getCurrentUserToken() : string{
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.token : null; 
    
  }

  getCurrentUserEmail() : string{
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.username : null; 
    
  }

  getRoleCurrentUserRole() : string{
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.role : null; 
  }

  loginWithGoogle(tokenId): Observable<LoginResponse>{
    return this.http.post<LoginResponse>(
      environment.apiUrl + "auth/login-google", 
      tokenId
    )
  }

  loginWithFacebook(tokenId):Observable<LoginResponse>{
    return this.http.post<LoginResponse>(
      environment.apiUrl + "auth/login-fb", 
      tokenId
    )
  }

  regularLogin(loginData): Observable<LoginResponse>{
    return this.http.post<LoginResponse>(
      environment.apiUrl + "auth/login",
      loginData      
    )

  }

  registration(newUser){
    console.log(newUser)
    return this.http.post(
      environment.apiUrl + "passengers/registration/",
      newUser
    )
  }

  
}
