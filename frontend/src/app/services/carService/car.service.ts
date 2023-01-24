import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Car, carImageMapper } from 'src/app/model/car/car';
import { CarType } from 'src/app/model/car/carType';
import { TextResponse } from 'src/app/model/response/textresponse';
import { ProfilePageData } from 'src/app/model/user/profileData';
import { Role } from 'src/app/model/user/role';
import { environment } from 'src/environments/environment';
import { HttpClientService } from '../custom-http/http-client.service';
import { UserService } from '../userService/user-sevice.service';

@Injectable({
  providedIn: 'root'
})
export class CarService {

  carTypes = {
    
  }

  constructor(private http: HttpClientService, 
              private userService: UserService) { }

  getCarByDriverId(id: number): Observable<Car>{
    return this.http.getT<Car>(environment.apiUrl + `cars?id=` + id);
  }

  public getProfilePageDataFromCar(car : Car) : ProfilePageData{
    let profileData = new ProfilePageData();
    profileData.title = `${car.model}`;
    profileData.subtitle = car.registration;    
    profileData.otherAttributes = {} as Record<string,any>;
    profileData.otherAttributes["max passengers"] = car.maxPassengers;
    profileData.otherAttributes["type"] = car.carType;
    profileData.otherAttributes["accomodations"] = car.accommodations;
    profileData.image =  "/assets/" + carImageMapper[car.carType];
    return profileData;
}

editCarData(car : Car) : Observable<TextResponse>{
  let saveChanges : boolean = true;
  if (this.userService.getRoleCurrentUserRole()=== Role.ROLE_DRIVER){
    saveChanges = false;
  }
  return this.http.putT<TextResponse>(environment.apiUrl + "cars/edit?saveChanges=" + saveChanges, car);
}
}
 