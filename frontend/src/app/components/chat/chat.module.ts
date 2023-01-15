import { NgModule } from '@angular/core';
import { JsonPipe } from '@angular/common';
import { AngularMaterialModule } from '../../modules/shared/angular-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedComponentsModule } from '../../modules/shared/shared-components.module';
import { CommonModule } from '@angular/common';
import { ChatBoxComponent } from './view/user-chat/chat-box.component';
import { MessageBoxComponent } from './components/message-box/message-box.component';
import { SentBoxComponent } from './components/sent-box/sent-box.component';
import { AdminChatComponent } from './view/admin-chat/admin-chat.component';
import { ChannelsListComponent } from './components/channel-list/channels-list/channels-list.component';
import { MatSelectionList } from '@angular/material/list';

@NgModule({
  declarations: [MessageBoxComponent, SentBoxComponent, ChatBoxComponent, AdminChatComponent, ChannelsListComponent],
  imports: [CommonModule,
            JsonPipe,
            ReactiveFormsModule,
            AngularMaterialModule,
            FormsModule,
            SharedComponentsModule],
  exports: [ChatBoxComponent],
  providers: [MatSelectionList]
})
export class ChatModule {}
