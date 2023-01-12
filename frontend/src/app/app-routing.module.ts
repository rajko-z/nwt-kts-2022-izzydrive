import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageUnLoggedComponent} from "./pages/home-page-un-logged/home-page-un-logged.component";
import {HomePageLoggedComponent} from "./pages/home-page-logged/home-page-logged.component";
import {AllDriversPageAdminComponent} from "./pages/all-drivers-page-admin/all-drivers-page-admin.component";
import {AllPassengersPageAdminComponent} from "./pages/all-passengers-page-admin/all-passengers-page-admin.component";
import {HomePageDriverComponent} from "./pages/home-page-driver/home-page-driver.component";

const routes: Routes = [
  {
    path: '',
    component: HomePageUnLoggedComponent,
  },
  {
    path: 'logged',
    component: HomePageLoggedComponent,
  },
  {
    path: 'onboard',
    loadChildren: () =>
      import('./components/onboarding/onboarding.module').then(m => m.OnboardingModule),
  },
  {
    path: 'profile',
    loadChildren: () =>
      import('./components/profile/profile.module').then(m => m.ProfileModule),
  },
  {
    path:'drivers',
    component: AllDriversPageAdminComponent
  },
  {
    path:'passengers',
    component: AllPassengersPageAdminComponent
  },
  {
    path:'driver',
    component: HomePageDriverComponent
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
