import {PlaceOnMap} from "../map/placeOnMap";
import {CarAccommodation} from "../car/carAccommodation";
import {IntermediateStationsOrderType, OptimalDrivingType} from "./driving";

export class DrivingFinderRequest {
  startLocation: PlaceOnMap;
  endLocation: PlaceOnMap;
  intermediateLocations: PlaceOnMap[];
  carAccommodation: CarAccommodation;
  intermediateStationsOrderType: IntermediateStationsOrderType;
  optimalDrivingType: OptimalDrivingType;
  linkedPassengersEmails: string[];
  reservation: boolean;
  scheduleTime?: string;
}
