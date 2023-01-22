import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import { EditCarComponent } from "src/app/components/profile/edit-car/edit-car.component";
import { ProfilePageCarComponent } from "src/app/components/profile/profile-page-car/profile-page-car.component";
import {HomePageDriverComponent} from "../../pages/home-page-driver/home-page-driver.component";
import {ReservationPageDriverComponent} from "../../pages/reservation-page-driver/reservation-page-driver.component";

const routes: Routes = [
  {
    path: '',
    component: HomePageDriverComponent
  },
  {
    path: 'current-drivings',
    component: HomePageDriverComponent
  },
  {
    path: 'reservation',
    component: ReservationPageDriverComponent
  },
  {
    path: 'car-data',
    component: ProfilePageCarComponent
  },
  {
    path: 'edit-car',
    component: EditCarComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule {}
