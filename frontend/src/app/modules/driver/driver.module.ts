import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DriverRoutingModule} from "./driver-routing.module";
import {HomePageDriverComponent} from "../../pages/home-page-driver/home-page-driver.component";
import {DisplayDrivingComponent} from "../../components/display-driving/display-driving.component";
import {ExplanationDialogComponent} from "../../components/explanation-dialog/explanation-dialog.component";
import {ReservationPageDriverComponent} from "../../pages/reservation-page-driver/reservation-page-driver.component";
import { ProfilePageCarComponent } from "src/app/components/profile/profile-page-car/profile-page-car.component";
import { ProfilePageComponent } from "src/app/components/shared/components/profile-page/profile-page.component";
import { UsersModule } from "../user/users.module";

const declaredModules = [
  HomePageDriverComponent,
  DisplayDrivingComponent,
  ExplanationDialogComponent,
  ReservationPageDriverComponent,
  ProfilePageCarComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    CommonModule,
    AngularMaterialModule,
    SharedComponentsModule,
    FormsModule,
    ReactiveFormsModule,
    DriverRoutingModule,
    UsersModule
  ],
  exports: declaredModules
})
export class DriverModule {}
