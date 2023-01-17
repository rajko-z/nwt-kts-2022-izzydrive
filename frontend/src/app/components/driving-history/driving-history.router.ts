import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DrivingHistoryComponent } from './driving-history.component';

const routes: Routes = [
  {
    path: 'driving',
    component: DrivingHistoryComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DrivingHistoryRouterModule {}
