import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
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

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
