export class Message{

    sender: string;
    text: string;
    timeStamp: Date;
    channel?: string;
    readDate?: Date;
    isSender?: boolean;

    constructor(sender: string,
        text: string,
        timeStamp: Date,
        isSender?: boolean,
        channel?: string,
        readDate?: Date,
        ){
            this.sender = sender;
            this.text = text;
            this.timeStamp = timeStamp;
            

    }
}