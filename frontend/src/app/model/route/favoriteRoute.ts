export class FavoriteRoute{
  routeId : number
  passengerId: number;
  startLocation: string;
  endLocation: string;
  intermediateLocations? : Array<String>
  
  constructor(
    passengerId: number,
    routeId? : number,
    startLocation?: string,
    endLocation?: string,
    intermediateLocations? : Array<String>,
    ){
      this.routeId = routeId;
      this.passengerId = passengerId;
      this.startLocation = startLocation;
      this.endLocation = endLocation;
      this.intermediateLocations = intermediateLocations;
    }
}
