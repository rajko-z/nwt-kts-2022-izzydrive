export class FavoriteRoute{
  passengerId: number;
  startLocation: string;
  endLocation: string;
  intermediateLocations? : Array<String>
  
  constructor(passengerId: number,
    startLocation: string,
    endLocation: string,
    intermediateLocations? : Array<String>){
      this.passengerId = passengerId;
      this.startLocation = startLocation;
      this.endLocation = endLocation;
      this.intermediateLocations = intermediateLocations;
    }
}
