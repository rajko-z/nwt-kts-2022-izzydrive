import {Location} from "./location";
import {PlaceOnMap} from "./placeOnMap";

export class CalculatedRoute {
  coordinates: Location[];
  distance: number;
  duration: number;
  reorderedIntermediate?: PlaceOnMap[];
}
