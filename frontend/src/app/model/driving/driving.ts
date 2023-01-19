import {Address} from "../address/address";
import {DrivingFinderRequest} from "./drivingFinderRequest.";
import {DrivingOption} from "./drivingOption";
import {RouteDTO} from "../route/route";
import {Driver} from "../driver/driver";
import {Location} from "../map/location";
import {CalculatedRoute} from "../map/calculatedRoute";

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

export class DrivingWithLocations {
  id: number;
  price: number;
  startDate: string;
  endDate: string;
  creationTime: string;
  route: RouteDTO;
  passengers: string[];
  isReservation: boolean;
  drivingState: DrivingState
  driver: Driver;
  fromDriverToStart: CalculatedRoute;
  fromStartToEnd: CalculatedRoute;
}

export enum DrivingState {
  INITIAL="INITIAL",
  PAYMENT="PAYMENT",
  WAITING="WAITING",
  ACTIVE="ACTIVE",
  FINISHED="FINISHED"
}
