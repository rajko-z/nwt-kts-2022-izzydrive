import { NgModule } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { LoginComponent } from './views/login/login.component';
import { OnboardingRouterModule } from './onboarding.router';
import { AngularMaterialModule } from '../shared/angular-material.module';
import { OnboardingHeaderComponent } from './components/onboarding-header/onboarding-header.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentsModule } from '../shared/shared-components.module';

@NgModule({
  declarations: [LoginComponent, OnboardingHeaderComponent],
  imports: [JsonPipe, ReactiveFormsModule, OnboardingRouterModule, AngularMaterialModule, FormsModule, SharedComponentsModule],
  exports: [],
})
export class OnboardingModule {}
