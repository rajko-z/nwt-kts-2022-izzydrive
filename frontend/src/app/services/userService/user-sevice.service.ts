import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from 'src/environments/environment';
import {LoginResponse} from 'src/app/model/response/loginResponse';
import {DomSanitizer} from '@angular/platform-browser';
import {Role} from 'src/app/model/user/role';
import {User} from 'src/app/model/user/user';
import {TextResponse} from 'src/app/model/response/textresponse';
import {ResetPassword} from 'src/app/model/user/resetPassword';
import {ProfilePageData} from 'src/app/model/user/profileData';
import {AdminRespondOnChanges} from 'src/app/model/message/AdminResponseOnChanges';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private _sanitizer: DomSanitizer)
  { }

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
    const currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.token : null;

  }

  getCurrentUserEmail() : string{
    const currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.email : null;
  }

  getRoleCurrentUserRole() : string{
    const currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.role : null;
  }

  getCurrentUserId() : number{
    const currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
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
    return this.http.post(
      environment.apiUrl + "passengers/registration/",
      newUser
    )
  }

  getCurrrentUserDataWithImg(): Observable<User>{ //:Observable<User>
    return this.http.get<User>( //<User>
      environment.apiUrl + "users/" + this.getCurrentUserEmail() + "?image=true", environment.header
    )
  }

  getCurrentUserData(): Observable<User>{
    return this.http.get<User>(
      environment.apiUrl + "users/" + this.getCurrentUserEmail())
  }

  changeUserData(user: User):Observable<TextResponse>{
    console.log(user)
    let saveChanges : boolean = true;
    if (this.getRoleCurrentUserRole()=== Role.ROLE_DRIVER){
      saveChanges = false;
    }
    console.log(saveChanges)
    return this.http.put<TextResponse>(
      environment.apiUrl + "users/change-info?saveChanges=" + saveChanges, user, {
        headers: this.createHeader()
      }
    );
  }

  getUserData(email : string): Observable<User>{
    return this.http.get<User>(
      environment.apiUrl + "users/" + email)
  }

  sendResetPasswordEmail(email : string):Observable<TextResponse>{
    return this.http.post<TextResponse>(environment.apiUrl + "users/reset-password-email", email);
 }

  verifyResetPasswordToken(token: string) {
    return this.http.get<TextResponse>(environment.apiUrl + "confirmation/reset-password?token=" + token);
  }

  resetPassword(resetPassword : ResetPassword) {
    return this.http.put<TextResponse>(environment.apiUrl + "users/reset-password" , resetPassword);
  }

  public getProfilePageDataFromUser(user : User) : ProfilePageData{
    let profileData = new ProfilePageData();
    profileData.title = `${user.firstName} ${user.lastName}`;
    profileData.subtitle = user.email;
    profileData.otherAttributes = {} as Record<string,any>;
    profileData.otherAttributes["phone number"] = user.phoneNumber;
    profileData.otherAttributes["role"] = user.role.substring(5);
    profileData.image = user.imageName? this._sanitizer.bypassSecurityTrustResourceUrl(`data:image/png;base64, ${user.imageName}`) : null;
    profileData.image = user.imageName?  this._sanitizer.bypassSecurityTrustResourceUrl(`data:image/png;base64, ${user.imageName}`) : null;
    return profileData;
  }

  adminRespondOnChanges(response: AdminRespondOnChanges){
    this.http.post(environment.apiUrl + 'users/response-changes', response).subscribe({
      next: (response) =>{
        console.log(response)
      },
      error: (error) => {
        console.log(error.error.message);
      }
    })
  }
}
