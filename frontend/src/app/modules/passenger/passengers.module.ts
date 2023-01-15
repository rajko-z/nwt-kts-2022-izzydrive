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

const declaredModules = [
  HomePageLoggedComponent,
  PaymentPageComponent,
  PayingInfoComponent,
  OrderingRideAdvancedComponent,
  RideDataFormComponent,
  OtherUsersDialogComponent,
  FavoriteRouteDialogComponent,
  NewRideLinkedUserComponent,
  DeniedRideLinkedUserComponent
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
