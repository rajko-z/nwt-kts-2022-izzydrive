import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { HttpClientModule } from '@angular/common/http';
import { HomePageLoggedComponent } from './pages/home-page-logged/home-page-logged.component';
import { HomePageUnLoggedComponent } from './pages/home-page-un-logged/home-page-un-logged.component';
import { MapComponent } from './components/map/map.component';
import { OrderingRideBasicComponent } from './components/ordering-ride-basic/ordering-ride-basic.component';
import { OrderingRideAdvancedComponent } from './components/ordering-ride-advanced/ordering-ride-advanced.component';
import { MenuModule } from './components/menu/menu.module';
import { ProfileComponent } from './components/profile/profile.component';
import { ProfileModule } from './components/profile/profile.module';
import { SharedComponentsModule } from './components/shared/shared-components.module';
import { AngularMaterialModule } from './components/shared/angular-material.module';

@NgModule({
  declarations: [AppComponent, ProfileComponent, HeaderComponent, HomePageLoggedComponent, HomePageUnLoggedComponent, MapComponent, OrderingRideBasicComponent, OrderingRideAdvancedComponent],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MenuModule,
    ProfileModule,
    SharedComponentsModule,
    AngularMaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
