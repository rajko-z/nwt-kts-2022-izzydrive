import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {EditProfileComponent} from "../../components/profile/edit-profile/edit-profile.component";

const routes: Routes = [
  {
    path: '',
    component: EditProfileComponent // za sad ovo, ali zameniti da bude profil korisnika
  },
  {
    path: 'edit-profile',
    component: EditProfileComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule {

}
