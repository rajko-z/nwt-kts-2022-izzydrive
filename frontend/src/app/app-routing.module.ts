import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'onboard',
    loadChildren: () =>
      import('./components/onboarding/onboarding.module').then(m => m.OnboardingModule),
  },
  {
    path: 'menu',
    loadChildren: () =>
      import('./components/menu/menu.module').then(m => m.MenuModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
