import { Token } from '@angular/compiler';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserSeviceService {

  constructor() { }

  setCurrentUser(userData : {email: string, token: string, role: string}){
    sessionStorage.setItem('currentUser', JSON.stringify({ token: userData.token, username: userData.email, role: userData.role }));
  }

  getTokenFromSessionStorage(){
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.token : null; 
    
  }

  getUsernameFromSessionStorage(){
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.username : null; 
    
  }

  getRoleCurrentUser(){
    var currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    return currentUser ? currentUser.role : null; 
  }

  
}
