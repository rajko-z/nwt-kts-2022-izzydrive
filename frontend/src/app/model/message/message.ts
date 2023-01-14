export class Message{

    sender: string;
    text: string;
    timeStamp: Date;
    channel?: string;
    channel_key?: string;

    constructor(sender: string,
        text: string,
        timeStamp: Date,
        channel_key?: string,
        channel?: string){
        
        this.sender = sender;
        this.text = text;
        this.timeStamp = timeStamp;
        this.channel_key = channel_key;
        this.channel = channel
        }
}