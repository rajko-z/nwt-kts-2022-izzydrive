export class AdminRespondOnChanges{
    driverEmail: string;
    response: string;

    constructor(driverEmail: string, response: string){
        this.driverEmail = driverEmail;
        this.response = response;
    }
}