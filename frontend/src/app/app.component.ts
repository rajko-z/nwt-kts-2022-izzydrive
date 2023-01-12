import { Component } from '@angular/core';
import { UserSeviceService } from './services/userService/user-sevice.service';
// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import  firebase from 'firebase/compat/app';
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAKK1keZb45wDnBvvsR5l6wk201kIZa1sE",
  authDomain: "izzydrive-368421.firebaseapp.com",
  projectId: "izzydrive-368421",
  storageBucket: "izzydrive-368421.appspot.com",
  messagingSenderId: "83743871907",
  appId: "1:83743871907:web:7e0f485634423a89aac016",
  measurementId: "G-F2L4CREP99",
  databaseURL: "https://izzydrive-368421-default-rtdb.europe-west1.firebasedatabase.app"
};

// Initialize Firebase

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
      title = 'NWT-KTS 2022 IZZYDRIVE';

      // isUserLoggedIn: boolean = false;

      constructor(private userService: UserSeviceService) {
        firebase.initializeApp(firebaseConfig);
      }
      
      ngOnInit(): void {
      }

      isUserLoggedIn() : boolean {
          return this.userService.isUserLoggedIn();
      }
}
