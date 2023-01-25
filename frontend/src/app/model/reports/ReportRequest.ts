export class ReportRequest{
    userId: number;
    startDate: Date;
    endDate: Date;

    constructor(userId: number, startDate: Date, endDate: Date){
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}