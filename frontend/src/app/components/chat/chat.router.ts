import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminChatComponent } from './view/admin-chat/admin-chat.component';
import { ChatBoxComponent } from './view/user-chat/chat-box.component';

const routes: Routes = [
  {
    path: 'support-chat',
    component: ChatBoxComponent
  },
  {
    path: 'admin-chat',
    component: AdminChatComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChatRouterModule {}
