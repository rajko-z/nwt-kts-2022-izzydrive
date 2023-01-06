import {icon, latLng, tileLayer} from "leaflet";

export function createMarkerOptionsForCar(tracking: boolean, driverStatus?: string) {
  let iconUrl =  '../../../assets/blue-car.png';
  let title = driverStatus;
  if (tracking) {
    iconUrl = '../../../assets/purple-car.png';
    title = "YOUR CAR";
  }
  return {
    icon: icon({
      iconUrl: iconUrl,
      iconSize: [45,40],
      iconAnchor: [18, 45]
    }),
    title: title
  };

}

export let markerOptionsStart = {
  icon: icon({
    iconUrl: '../../../assets/rocket-launch.png',
    iconSize: [50,50],
    iconAnchor: [18, 45],
  }),
  title: 'Start location'
}

export let markerOptionsEnd = {
  icon: icon({
    iconUrl: '../../../assets/race-flag.png',
    iconSize: [45,45],
    iconAnchor: [18, 45],
  }),
  title: 'End location'
}
