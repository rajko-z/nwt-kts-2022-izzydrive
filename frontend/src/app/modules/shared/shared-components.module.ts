import {CommonModule, JsonPipe} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AngularMaterialModule} from './angular-material.module';
import {
  BaseUserDataFormComponent
} from '../../components/shared/components/base-user-data-form/base-user-data-form.component';
import {
  BaseCarDataFormComponent
} from '../../components/shared/components/base-car-data-form/base-car-data-form.component';
import {MapComponent} from "../../components/map/map.component";
import {SearchPlaceComponent} from "../../components/ordering-ride/shared/search-place/search-place.component";
import {RideDataTableComponent} from "../../components/ordering-ride/shared/ride-data-table/ride-data-table.component";
import {
  OverviewOrderingRideComponent
} from "../../components/ordering-ride/advanced/overview-ordering-ride/overview-ordering-ride.component";
import {LeafletModule} from "@asymmetrik/ngx-leaflet";
import {HomePageComponent} from "../../pages/home-page/home-page.component";
import {RouterLinkWithHref} from "@angular/router";
import {ReviewRideTableComponent} from 'src/app/components/shared/review-ride-table/review-ride-table.component';
import {DetailRideViewComponent} from "../../components/detail-ride-view/detail-ride-view.component";
import {CardOptionComponent} from "../../components/ordering-ride/shared/card-option/card-option.component";
import {
  NotificationComponent
} from "../../components/notifications/notification-review/notification/notification.component";
import {
  NotificationImageComponent
} from "../../components/notifications/notification-review/notification-image/notification-image.component";
import {
  DriverOverviewComponent
} from "../../components/shared/overivew-ride-components/driver-overview/driver-overview.component";
import {
  RideOverviewComponent
} from "../../components/shared/overivew-ride-components/ride-overview/ride-overview.component";
import {
  PassengerOverviewComponent
} from "../../components/shared/overivew-ride-components/passenger-overview/passenger-overview.component";

const declaredModules = [
  BaseUserDataFormComponent,
  BaseCarDataFormComponent,
  MapComponent,
  SearchPlaceComponent,
  RideDataTableComponent,
  OverviewOrderingRideComponent,
  ReviewRideTableComponent,
  HomePageComponent,
  DetailRideViewComponent,
  CardOptionComponent,
  NotificationComponent,
  NotificationImageComponent,
  DriverOverviewComponent,
  RideOverviewComponent,
  PassengerOverviewComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    AngularMaterialModule,
    CommonModule,
    JsonPipe,
    ReactiveFormsModule,
    FormsModule,
    LeafletModule,
    RouterLinkWithHref
  ],
  exports: declaredModules,
})
export class SharedComponentsModule {
}
