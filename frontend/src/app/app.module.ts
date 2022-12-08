import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { HttpClientModule } from '@angular/common/http';
import { MenuModule } from './components/menu/menu.module';
import { ProfileComponent } from './components/profile/profile.component';
import { ProfileModule } from './components/profile/profile.module';
import { SharedComponentsModule } from './components/shared/shared-components.module';

@NgModule({
  declarations: [AppComponent, HeaderComponent, ProfileComponent],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MenuModule,
    ProfileModule,
    SharedComponentsModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
