import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {AdminRoutingModule} from "./admin-routing.module";
import {AllDriversPageAdminComponent} from "../../pages/all-drivers-page-admin/all-drivers-page-admin.component";
import {
  AllPassengersPageAdminComponent
} from "../../pages/all-passengers-page-admin/all-passengers-page-admin.component";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {HomePageAdminComponent} from "../../pages/home-page-admin/home-page-admin.component";
import {AddDriverComponent} from "../../components/profile/add-driver/add-driver.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ChatModule} from "../../components/chat/chat.module";
import {ReviewUsersTableComponent} from "src/app/components/shared/review-users-table/review-users-table.component";
import {
  RejectRideDriverComponent
} from "../../components/notifications/reject-ride-driver/reject-ride-driver.component";
import {
  ReviewAndWriteAdminNotesComponent
} from "../../components/review-and-write-admin-notes/review-and-write-admin-notes.component";
import {
  DriverChangeInfoComponent
} from "src/app/components/notifications/driver-change-info/driver-change-info.component";
import {CarChangeInfoComponent} from "src/app/components/notifications/car-change-info/car-change-info.component";
import {ReportedDriverComponent} from "../../components/notifications/reported-driver/reported-driver.component";
import {
  AdminLeftPartHomeComponent
} from "../../pages/home-page-admin/admin-left-part-home/admin-left-part-home.component";
import {AdminSearchRideComponent} from "../../pages/home-page-admin/admin-search-ride/admin-search-ride.component";
import {RideCardListComponent} from "../../pages/home-page-admin/ride-card-list/ride-card-list.component";
import {RideCardComponent} from "../../pages/home-page-admin/ride-card-list/ride-card/ride-card.component";
import {NoteListComponent} from "../../components/review-and-write-admin-notes/note-list/note-list.component";

const declaredModules = [
  AllDriversPageAdminComponent,
  AllPassengersPageAdminComponent,
  ReviewUsersTableComponent,
  HomePageAdminComponent,
  AddDriverComponent,
  RejectRideDriverComponent,
  ReviewAndWriteAdminNotesComponent,
  DriverChangeInfoComponent,
  CarChangeInfoComponent,
  ReportedDriverComponent,
  AdminLeftPartHomeComponent,
  AdminSearchRideComponent,
  RideCardListComponent,
  RideCardComponent,
  NoteListComponent
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
    ChatModule
  ],
  exports: declaredModules
})
export class AdminModule {}
