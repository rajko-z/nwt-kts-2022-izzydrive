import { NgModule } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { AngularMaterialModule } from '../shared/angular-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentsModule } from '../shared/shared-components.module';
import { CommonModule } from '@angular/common';
import { ChatBoxComponent } from './view/chat-box/chat-box.component';
import { ChatRouterModule } from './chat.router';
import { MessageBoxComponent } from './components/message-box/message-box.component';
import { SentBoxComponent } from './components/sent-box/sent-box.component';

@NgModule({
  declarations: [MessageBoxComponent, SentBoxComponent, ChatBoxComponent],
  imports: [CommonModule, 
            JsonPipe, 
            ReactiveFormsModule, 
            AngularMaterialModule, 
            FormsModule, 
            SharedComponentsModule,
            ChatRouterModule],
  exports: [ChatBoxComponent],
  providers: []
})
export class ChatModule {}
