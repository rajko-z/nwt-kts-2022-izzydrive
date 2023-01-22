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
import { EditCarComponent } from "src/app/components/profile/edit-car/edit-car.component";

const declaredModules = [
  HomePageDriverComponent,
  DisplayDrivingComponent,
  ExplanationDialogComponent,
  ReservationPageDriverComponent,
  ProfilePageCarComponent,
  EditCarComponent
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
