import {NgModule} from "@angular/core";
import {ChangePasswordComponent} from "../../components/change-password/change-password.component";
import {EditProfileComponent} from "../../components/profile/edit-profile/edit-profile.component";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {UsersRoutingModule} from "./users-routing.module";
import { DrivingHistoryComponent } from "src/app/components/driving-history/driving-history.component";
import { SharedComponentsModule } from "../shared/shared-components.module";

const declaredModules = [
  ChangePasswordComponent,
  EditProfileComponent,
  DrivingHistoryComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    CommonModule,
    FormsModule,
    AngularMaterialModule,
    UsersRoutingModule,
    ReactiveFormsModule,
    SharedComponentsModule
  ],
  exports: declaredModules
})
export class UsersModule {}
