import {NgModule} from "@angular/core";
import {ChangePasswordComponent} from "../../components/change-password/change-password.component";
import {EditProfileComponent} from "../../components/profile/edit-profile/edit-profile.component";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {UsersRoutingModule} from "./users-routing.module";
import { DrivingHistoryComponent } from "src/app/components/driving-history/driving-history.component";
import { SharedComponentsModule } from "../shared/shared-components.module";
import {
  NotificationReviewComponent
} from "../../components/notifications/notification-review/notification-review.component";
import { ProfilePageComponent } from "src/app/components/shared/components/profile-page/profile-page.component";
import { ProfilePageUserComponent } from "src/app/components/profile/profile-page-user/profile-page-user.component";
import { NgApexchartsModule } from "ng-apexcharts";
import { DrivingNumberReportComponent } from "src/app/components/reports/components/driving-number-report/driving-number-report.component";
import { ReportsViewComponent } from "src/app/components/reports/view/reports-view/reports-view.component";

const declaredModules = [
  ChangePasswordComponent,
  EditProfileComponent,
  DrivingHistoryComponent,
  NotificationReviewComponent,
  ProfilePageComponent,
  ProfilePageUserComponent,
  DrivingNumberReportComponent,
  ReportsViewComponent
];

@NgModule({
  declarations: declaredModules,
    imports: [
        CommonModule,
        FormsModule,
        AngularMaterialModule,
        UsersRoutingModule,
        ReactiveFormsModule,
        SharedComponentsModule,
        NgApexchartsModule
    ],
  exports: declaredModules
})
export class UsersModule {}
