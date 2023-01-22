import { SafeResourceUrl } from "@angular/platform-browser";
import { ca } from "date-fns/locale";
import { Car } from "../car/car";
import { User } from "./user";

export class ProfilePageData{
    title: string;
    subtitle: string;
    image: SafeResourceUrl;
    otherAttributes: Record<string, any>;

    public getProfilePageDataFromCar(car : Car) : ProfilePageData{
        let profileData = new ProfilePageData();
        profileData.title = `${car.carType}`;
        profileData.subtitle = car.registration;
        profileData.otherAttributes["max passengers"] = car.maxPassengers
        Object.keys(car.carAccommodation).map((key) => {
            profileData.otherAttributes[key]= car.carAccommodation[key];
        })
        return profileData;
    }
}


