import {NgModule} from '@angular/core';
import {PreloadAllModules, RouterModule, Routes} from '@angular/router';
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {HomepageGuard} from "./guards/homepage.guard";
import {DriverGuard} from "./guards/driver.guard";
import {PassengerGuard} from "./guards/passenger.guard";
import {AdminGuard} from "./guards/admin.guard";
import {AnonymousGuard} from "./guards/anonymous.guard";
import {UserGuard} from "./guards/user.guard";

const routes: Routes = [
  {
    path: 'anon',
    canActivate: [AnonymousGuard],
    loadChildren: () => import('./modules/anonymous/anonymous.module').then(m => m.AnonymousModule)
  },
  {
    path: 'user',
    canActivate: [UserGuard],
    loadChildren: () => import('./modules/user/users.module').then(m => m.UsersModule)
  },
  {
    path: 'admin',
    canActivate: [AdminGuard],
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule)
  },
  {
    path: 'passenger',
    canActivate: [PassengerGuard],
    loadChildren: () => import('./modules/passenger/passengers.module').then(m => m.PassengersModule)
  },
  {
    path: 'driver',
    canActivate: [DriverGuard],
    loadChildren: () => import('./modules/driver/driver.module').then(m => m.DriverModule)
  },
  {
    path: '**',
    component: HomePageComponent,
    canActivate: [HomepageGuard],
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      {
        preloadingStrategy: PreloadAllModules
      }
    )
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
