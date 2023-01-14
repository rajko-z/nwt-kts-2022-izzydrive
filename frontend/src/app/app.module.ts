import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CommonModule, DatePipe} from '@angular/common';
import {HeaderComponent} from './components/header/header.component';
import {HttpClientModule} from '@angular/common/http';
import {HomePageLoggedComponent} from './pages/home-page-logged/home-page-logged.component';
import {HomePageUnLoggedComponent} from './pages/home-page-un-logged/home-page-un-logged.component';
import {MapComponent} from './components/map/map.component';
import {OrderingRideBasicComponent} from './components/ordering-ride/basic/ordering-ride-basic.component';
import {OrderingRideAdvancedComponent} from './components/ordering-ride/advanced/ordering-ride-advanced.component';
import {MenuModule} from './components/menu/menu.module';
import {ProfileComponent} from './components/profile/profile.component';
import {ProfileModule} from './components/profile/profile.module';
import {SharedComponentsModule} from './components/shared/shared-components.module';
import {AngularMaterialModule} from './components/shared/angular-material.module';
import {AllPassengersPageAdminComponent} from './pages/all-passengers-page-admin/all-passengers-page-admin.component';
import {AllDriversPageAdminComponent} from './pages/all-drivers-page-admin/all-drivers-page-admin.component';
import {ReviewUsersTableComponent} from './components/review-users-table/review-users-table.component';
import {ReviewRideTableComponent} from './components/review-ride-table/review-ride-table.component';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FavoriteRouteDialogComponent} from './components/favorite-route-dialog/favorite-route-dialog.component';
import {ReactiveFormsModule} from "@angular/forms";
import {
  IntermediateStationsDialogComponent
} from './components/intermediate-stations-dialog/intermediate-stations-dialog.component';
import {OtherUsersDialogComponent} from './components/other-users-dialog/other-users-dialog.component';
import {HomePageDriverComponent} from './pages/home-page-driver/home-page-driver.component';
import {DisplayDrivingComponent} from './components/display-driving/display-driving.component';
import {ExplanationDialogComponent} from './components/explanation-dialog/explanation-dialog.component';
import {ChatModule} from './components/chat/chat.module';
import {InjectableRxStompConfig, RxStompService, rxStompServiceFactory} from '@stomp/ng2-stompjs';
import {RideDataFormComponent} from './components/ordering-ride/advanced/ride-data-form/ride-data-form.component';
import {RideDataTableComponent} from './components/ordering-ride/shared/ride-data-table/ride-data-table.component';
import {
  OverviewOrderingRideComponent
} from './components/ordering-ride/advanced/overview-ordering-ride/overview-ordering-ride.component';
import {LeafletModule} from '@asymmetrik/ngx-leaflet';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {
  RideDataBasicFormComponent
} from "./components/ordering-ride/basic/ride-data-basic-form/ride-data-basic-form.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {SearchPlaceComponent} from './components/ordering-ride/shared/search-place/search-place.component';
import {MatExpansionModule} from "@angular/material/expansion";
import {PaymentPageComponent} from './pages/payment-page/payment-page.component';
import {AngularFireModule} from "@angular/fire/compat";

import {AngularFireMessagingModule} from "@angular/fire/compat/messaging";
import {environment} from "../environments/environment";
import { NewRideLinkedUserComponent } from './components/notifications/new-ride-linked-user/new-ride-linked-user.component';


@NgModule({
  declarations: [AppComponent,
                 ProfileComponent,
                 HeaderComponent,
                 HomePageLoggedComponent,
                 HomePageUnLoggedComponent,
                 MapComponent,
                 OrderingRideBasicComponent,
                 OrderingRideAdvancedComponent,
                 AllDriversPageAdminComponent,
                 ReviewUsersTableComponent,
                 ReviewRideTableComponent,
                 AllPassengersPageAdminComponent,
                 FavoriteRouteDialogComponent,
                 IntermediateStationsDialogComponent,
                 OtherUsersDialogComponent,
                 HomePageDriverComponent,
                 DisplayDrivingComponent,
                 RideDataFormComponent,
                 RideDataTableComponent,
                 OverviewOrderingRideComponent,
                 ExplanationDialogComponent,
                 RideDataBasicFormComponent,
                 SearchPlaceComponent,
                 PaymentPageComponent,
                 NewRideLinkedUserComponent],

    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        HttpClientModule,
        MenuModule,
        ProfileModule,
        ChatModule,
        SharedComponentsModule,
        AngularMaterialModule,
        ReactiveFormsModule,
        DatePipe,
        LeafletModule,
        MatAutocompleteModule,
        MatProgressSpinnerModule,
        MatExpansionModule,
        AngularFireModule.initializeApp(environment.firebaseConfig),
        AngularFireMessagingModule,
    ],
    exports: [],
    providers: [{ provide: MAT_DIALOG_DATA, useValue: {} },
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
