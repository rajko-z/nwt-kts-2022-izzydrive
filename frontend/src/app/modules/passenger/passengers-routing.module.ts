import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomePageLoggedComponent} from "../../pages/home-page-logged/home-page-logged.component";
import {PaymentPageComponent} from "../../pages/payment-page/payment-page.component";

const routes: Routes = [
  {
    path: '',
    component: HomePageLoggedComponent,
  },
  {
    path: 'order-now',
    component: HomePageLoggedComponent,
  },
  {
    path: 'payment',
    component: PaymentPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PassengersRoutingModule {}
