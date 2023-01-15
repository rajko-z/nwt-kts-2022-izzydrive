import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {AdminRoutingModule} from "./admin-routing.module";
import {AllDriversPageAdminComponent} from "../../pages/all-drivers-page-admin/all-drivers-page-admin.component";
import {
  AllPassengersPageAdminComponent
} from "../../pages/all-passengers-page-admin/all-passengers-page-admin.component";
import {ReviewUsersTableComponent} from "../../components/review-users-table/review-users-table.component";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {HomePageAdminComponent} from "../../pages/home-page-admin/home-page-admin.component";
import {AddDriverComponent} from "../../components/profile/add-driver/add-driver.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

const declaredModules = [
  AllDriversPageAdminComponent,
  AllPassengersPageAdminComponent,
  ReviewUsersTableComponent,
  HomePageAdminComponent,
  AddDriverComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    CommonModule,
    AngularMaterialModule,
    AdminRoutingModule,
    SharedComponentsModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  exports: declaredModules
})
export class AdminModule {}