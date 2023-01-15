export class Message{

    sender: string;
    text: string;
    timeStamp: string;
    channel?: string;
    channel_key?: string;

    constructor(sender: string,
        text: string,
        timeStamp: string,
        channel_key?: string,
        channel?: string){
        
        this.sender = sender;
        this.text = text;
        this.timeStamp = timeStamp;
        this.channel_key = channel_key;
        this.channel = channel
        }
}