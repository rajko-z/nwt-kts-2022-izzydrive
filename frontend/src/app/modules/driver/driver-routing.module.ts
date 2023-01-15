import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomePageDriverComponent} from "../../pages/home-page-driver/home-page-driver.component";

const routes: Routes = [
  {
    path: '',
    component: HomePageDriverComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule {}
