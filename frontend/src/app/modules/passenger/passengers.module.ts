import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";
import {PassengersRoutingModule} from "./passengers-routing.module";
import {HomePageLoggedComponent} from "../../pages/home-page-logged/home-page-logged.component";
import {PaymentPageComponent} from "../../pages/payment-page/payment-page.component";
import {PayingInfoComponent} from "../../components/paying-info/paying-info.component";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {ReactiveFormsModule} from "@angular/forms";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {OrderingRideAdvancedComponent} from "../../components/ordering-ride/advanced/ordering-ride-advanced.component";
import {RideDataFormComponent} from "../../components/ordering-ride/advanced/ride-data-form/ride-data-form.component";
import {OtherUsersDialogComponent} from "../../components/other-users-dialog/other-users-dialog.component";
import {FavoriteRouteDialogComponent} from "../../components/favorite-route-dialog/favorite-route-dialog.component";
import {
  NewRideLinkedUserComponent
} from "../../components/notifications/new-ride-linked-user/new-ride-linked-user.component";
import {
  DeniedRideLinkedUserComponent
} from "../../components/notifications/denied-ride-linked-user/denied-ride-linked-user.component";
import { NewReservationComponent } from '../../components/notifications/new-reservation/new-reservation.component';
import { EvaluationComponent } from "src/app/components/driving-history/evaluation/evaluation.component";
import {CurrentDrivingPageComponent} from "../../pages/current-driving-page/current-driving-page.component";
import {
  CurrentDrivingPassengerComponent
} from "../../components/current-driving-passenger/current-driving-passenger.component";
import { ReservationsListComponent } from "src/app/components/reservations-list/reservations-list.component";
import { ConfirmCancelReservationComponent } from "src/app/components/notifications/confirm-cancel-reservation/confirm-cancel-reservation.component";
import { FavouriteRoutsComponent } from "src/app/components/favorite-routs/favorite-routs.component";

const declaredModules = [
  HomePageLoggedComponent,
  PaymentPageComponent,
  PayingInfoComponent,
  OrderingRideAdvancedComponent,
  RideDataFormComponent,
  OtherUsersDialogComponent,
  FavoriteRouteDialogComponent,
  NewRideLinkedUserComponent,
  DeniedRideLinkedUserComponent,
  NewReservationComponent,
  EvaluationComponent,
  CurrentDrivingPageComponent,
  CurrentDrivingPassengerComponent,
  ReservationsListComponent,
  ConfirmCancelReservationComponent,
  FavouriteRoutsComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    RouterModule,
    CommonModule,
    PassengersRoutingModule,
    AngularMaterialModule,
    ReactiveFormsModule,
    SharedComponentsModule,
  ],
  exports: declaredModules
})
export class PassengersModule {}
