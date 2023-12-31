import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import { ReservationsListComponent } from "src/app/components/reservations-list/reservations-list.component";
import {HomePageLoggedComponent} from "../../pages/home-page-logged/home-page-logged.component";
import {PaymentPageComponent} from "../../pages/payment-page/payment-page.component";
import {CurrentDrivingPageComponent} from "../../pages/current-driving-page/current-driving-page.component";
import { FavouriteRoutsComponent } from "src/app/components/favorite-routs/view/favorite-routs.component";

const routes: Routes = [
  {
    path: '',
    component: HomePageLoggedComponent,
    data: {
      reservation: false
    }
  },
  {
    path: 'current-driving',
    component: CurrentDrivingPageComponent
  },
  {
    path: 'order-now',
    component: HomePageLoggedComponent,
    data: {
      reservation: false
    }
  },
  {
    path: 'order-for-later',
    component: HomePageLoggedComponent,
    data: {
      reservation: true
    }
  },
  {
    path: 'payment',
    component: PaymentPageComponent
  },
  {
    path: 'reservations',
    component: ReservationsListComponent
  },
  {
    path: 'favorites',
    component: FavouriteRoutsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PassengersRoutingModule {}
