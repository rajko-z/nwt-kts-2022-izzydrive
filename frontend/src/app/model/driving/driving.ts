import {Address} from "../address/address";
import {DrivingFinderRequest} from "./drivingFinderRequest.";
import {DrivingOption} from "./drivingOption";

export enum OptimalDrivingType {
  NO_PREFERENCE,
  CHEAPEST_RIDE,
  SHORTEST_TRAVEL_TIME
}

export enum IntermediateStationsOrderType {
  IN_ORDER,
  SYSTEM_CALCULATE
}

export class DrivingRequest {
  drivingFinderRequest: DrivingFinderRequest;
  drivingOption: DrivingOption;
}

export class Driving {
  id: number;
  price: number;
  startDate?: Date;
  endDate?: Date;
  passengers: Array<String>;
  start: Address;
  end: Address;
  intermediateStations?: Array<String>;
  evaluationAvailable? : boolean;
}
