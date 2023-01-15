import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AllDriversPageAdminComponent} from "../../pages/all-drivers-page-admin/all-drivers-page-admin.component";
import {
  AllPassengersPageAdminComponent
} from "../../pages/all-passengers-page-admin/all-passengers-page-admin.component";
import {HomePageAdminComponent} from "../../pages/home-page-admin/home-page-admin.component";
import {AddDriverComponent} from "../../components/profile/add-driver/add-driver.component";
import {AdminChatComponent} from "../../components/chat/view/admin-chat/admin-chat.component";

const routes: Routes = [
  {
    path: '',
    component: HomePageAdminComponent
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
    path: 'add-driver',
    component: AddDriverComponent
  },
  {
    path: 'admin-chat',
    component: AdminChatComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
