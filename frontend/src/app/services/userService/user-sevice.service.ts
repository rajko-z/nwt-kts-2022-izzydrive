import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Token } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginResponse } from 'src/app/model/response/loginResponse';
import { User } from 'src/app/model/user/user';
import { FormControl } from '@angular/forms';
import { HttpClientService } from '../custom-http/http-client.service';
import { DomSanitizer } from '@angular/platform-browser';
import { Role } from 'src/app/model/user/role';

@Injectable({
  providedIn: 'root'
})
export class UserSeviceService {

  constructor(private http: HttpClient, private _sanitizer: DomSanitizer) { }
 
  createHeader(){
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
       'Authorization': "Bearer "+ this.getCurrentUserToken()
    });
    return headers;
  }

  isUserLoggedIn(): boolean {
    return this.getCurrentUserToken() !== null
  }
  
  setCurrentUser(userData : {email: string, token: string, role: Role, id: number}){
    sessionStorage.setItem('currentUser', JSON.stringify({email: userData.email, token: userData.token, role: userData.role , id: userData.id}));
  }

  getCurrentUserToken() : string{
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.token : null; 
    
  }

  getCurrentUserEmail() : string{
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.email : null; 
    
  }

  getRoleCurrentUserRole() : string{
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.role : null; 
  }

  getCurrentUserId() : number{
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.id : null; 
    
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

  getProfilePhoto(username: string){
    return this.http.get<String>(
      environment.apiUrl + "users/profile-img/" +  username, environment.header
    )
  }

  getProfilePhotoCurrentUser(){
    let id: number = this.getCurrentUserId();
    console.log(environment.header);
    this.http.get(
      environment.apiUrl + `users/profile-img/${id}`, {responseType: 'text'}
    ).subscribe({
      next: (response) =>{
          return this._sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + response);
      },
      error: (response) =>{
          return "/assets/404-error.png"
      }
    })
  }

  getCurrrentUserDataWithImg(): Observable<User>{ //:Observable<User>
    return this.http.get<User>( //<User>
      environment.apiUrl + "users/" + this.getCurrentUserEmail() + "?image=true", environment.header
    )
  }

  getCurrentUserName(): Observable<User>{
    return this.http.get<User>( 
      environment.apiUrl + "users/" + this.getCurrentUserEmail())
  }

  changeUserData(user: User){
    return this.http.put(
      environment.apiUrl + "users/change-info", user, {
        headers: this.createHeader()
      }
    );
  }

  
}
