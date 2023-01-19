import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CommonModule, DatePipe} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {MenuModule} from './components/menu/menu.module';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {ReactiveFormsModule} from "@angular/forms";
import {ChatModule} from './components/chat/chat.module';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from '@stomp/ng2-stompjs';
import firebase from "firebase/compat/app";
import {AngularFireModule} from "@angular/fire/compat";

import {AngularFireMessagingModule} from "@angular/fire/compat/messaging";
import {environment} from "../environments/environment";
import {HeaderComponent} from "./components/header/header.component";
import { ConfirmCancelReservationComponent } from './components/notifications/confirm-cancel-reservation/confirm-cancel-reservation.component';


firebase.initializeApp(environment.firebaseConfig);

@NgModule({
  declarations: [AppComponent, HeaderComponent, ConfirmCancelReservationComponent],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MenuModule,
    ChatModule,
    ReactiveFormsModule,
    DatePipe,
    AngularFireModule.initializeApp(environment.firebaseConfig),
    AngularFireMessagingModule,
  ],
  exports: [],
  providers: [{provide: MAT_DIALOG_DATA, useValue: {}},
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    },
    DatePipe
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}

