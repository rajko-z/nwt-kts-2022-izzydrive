import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageUnLoggedComponent} from "./pages/home-page-un-logged/home-page-un-logged.component";
import {HomePageLoggedComponent} from "./pages/home-page-logged/home-page-logged.component";

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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
