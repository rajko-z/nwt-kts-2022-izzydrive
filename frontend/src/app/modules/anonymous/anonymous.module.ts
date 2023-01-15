import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AnonymousRoutingModule} from "./anonymous-routing.module";
import {HomePageUnLoggedComponent} from "../../pages/home-page-un-logged/home-page-un-logged.component";
import {OrderingRideBasicComponent} from "../../components/ordering-ride/basic/ordering-ride-basic.component";
import {
  RideDataBasicFormComponent
} from "../../components/ordering-ride/basic/ride-data-basic-form/ride-data-basic-form.component";
import {OnboardingModule} from "../../components/onboarding/onboarding.module";

const declaredModules = [
  HomePageUnLoggedComponent,
  OrderingRideBasicComponent,
  RideDataBasicFormComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    CommonModule,
    AngularMaterialModule,
    AnonymousRoutingModule,
    SharedComponentsModule,
    FormsModule,
    ReactiveFormsModule,
    OnboardingModule
  ],
  exports: declaredModules
})
export class AnonymousModule {}
