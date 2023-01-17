export class Evaluation {
    text: string;
    drivingId: number;
    driverRate: number;
    vehicleGrade: number;

    constructor(text: string,
        drivingId: number,
        driverRate: number,
        vehicleGrade: number,){
        this.text = text;
        this.driverRate = driverRate;
        this.drivingId = drivingId;
        this.vehicleGrade = vehicleGrade;
    }
}