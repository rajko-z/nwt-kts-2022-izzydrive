import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomePageUnLoggedComponent} from "../../pages/home-page-un-logged/home-page-un-logged.component";
import {LoginComponent} from "../../components/onboarding/views/login/login.component";
import {RegisterComponent} from "../../components/onboarding/views/register/register.component";

const routes: Routes = [
  {
    path: '',
    component: HomePageUnLoggedComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnonymousRoutingModule {}
