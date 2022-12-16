import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MenuComponent } from './menu.component';
import { AddDriverComponent } from '../profile/add-driver/add-driver.component';

const routes: Routes = [
  {
    path: '',
    component: MenuComponent
  },
  {
    path: 'add-driver',
    component: AddDriverComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MenuRouterModule {}