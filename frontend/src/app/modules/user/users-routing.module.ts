import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {DrivingHistoryComponent} from "src/app/components/driving-history/driving-history.component";
import {EditProfileComponent} from "../../components/profile/edit-profile/edit-profile.component";
import {
  NotificationReviewComponent
} from "../../components/notifications/notification-review/notification-review.component";
import { ProfilePageComponent } from "src/app/components/shared/components/profile-page/profile-page.component";
import { ProfilePageUserComponent } from "src/app/components/profile/profile-page-user/profile-page-user.component";
import { DrivingReportComponent } from "src/app/components/reports/components/driving-report/driving-report.component";
import { ReportsViewComponent } from "src/app/components/reports/view/reports-view/reports-view.component";

const routes: Routes = [
  {
    path: '',
    component: EditProfileComponent // za sad ovo, ali zameniti da bude profil korisnika
  },
  {
    path: 'edit-profile',
    component: EditProfileComponent,
  },
  {
    path: 'driving-history',
    component: DrivingHistoryComponent,
  },
  {
    path: 'notifications',
    component: NotificationReviewComponent,
  },
  {
    path: 'profile-page',
    component: ProfilePageUserComponent,
  },
  {
    path: 'reports',
    component: ReportsViewComponent,
  }
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule {

}
