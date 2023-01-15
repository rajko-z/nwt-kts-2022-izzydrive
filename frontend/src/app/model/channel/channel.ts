import  firebase from "firebase/compat/app";

export class Channel{
    id: string;
    name: string;
    open_by_admin : boolean;
    open_by_user: boolean;
    unread_messages_by_admin : boolean;
    unread_messages_by_user : boolean;
    isEmpty : boolean;

    constructor(id: string,
        name: string,
        open_by_admin : boolean,
        open_by_user: boolean,
        unread_messages_by_admin : boolean,
        unread_messages_by_user : boolean,
        isEmpty? : boolean){
            this.id = id;
            this.name = name;
            this.open_by_admin = open_by_admin;
            this.open_by_user = open_by_user;
            this.unread_messages_by_admin = unread_messages_by_admin;
            this.unread_messages_by_user = unread_messages_by_user;
            this.isEmpty = isEmpty ? isEmpty : false
        }

}