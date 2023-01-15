import { NgModule } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { LoginComponent } from './views/login/login.component';
import { AngularMaterialModule } from '../../modules/shared/angular-material.module';
import { OnboardingHeaderComponent } from './components/onboarding-header/onboarding-header.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentsModule } from '../../modules/shared/shared-components.module';
import { CommonModule } from '@angular/common';
import {
  FacebookLoginProvider,
  SocialLoginModule,
  SocialAuthServiceConfig,
} from '@abacritt/angularx-social-login';
import { GoogleLoginProvider } from '@abacritt/angularx-social-login';
import { RegisterComponent } from './views/register/register.component';

@NgModule({
  declarations: [LoginComponent, OnboardingHeaderComponent, RegisterComponent],
  imports: [SocialLoginModule,
            CommonModule,
            JsonPipe,
            ReactiveFormsModule,
            AngularMaterialModule,
            FormsModule,
            SharedComponentsModule],
  exports: [],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('430844285930561'),
          },
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('83743871907-93ba97v7t6j53p316sl6js0pgj9hmp51.apps.googleusercontent.com'),
          },
        ],
      } as SocialAuthServiceConfig,
    },
  ]
})
export class OnboardingModule {}
