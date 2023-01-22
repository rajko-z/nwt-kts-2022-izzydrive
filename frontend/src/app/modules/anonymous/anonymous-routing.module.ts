import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomePageUnLoggedComponent} from "../../pages/home-page-un-logged/home-page-un-logged.component";
import {LoginComponent} from "../../components/onboarding/views/login/login.component";
import {RegisterComponent} from "../../components/onboarding/views/register/register.component";
import { ResetPasswordComponent } from "src/app/components/profile/reset-password/reset-password.component";

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
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent,
  }, 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnonymousRoutingModule {}
