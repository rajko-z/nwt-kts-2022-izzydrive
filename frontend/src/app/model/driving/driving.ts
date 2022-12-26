import {Address} from "../address/address";

export class Driving{
  private id: number;
  private price: number;
  private startDate: Date;
  private endDate: Date;
  private passengers: Array<String>;
  private start: Address;
  private end: Address;
}
