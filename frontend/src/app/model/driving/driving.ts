import {Address} from "../address/address";

export class Driving{
  private id: number;
  price: number;
  startDate?: Date;
  endDate?: Date;
  passengers: Array<String>;
  start: Address;
  end: Address;
}
