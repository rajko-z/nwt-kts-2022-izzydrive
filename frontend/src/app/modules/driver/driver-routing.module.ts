import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
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
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule {}
