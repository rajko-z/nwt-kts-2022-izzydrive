import { Injectable } from '@angular/core';
import {environment} from "../../../environments/environment";
import {HttpClientService} from "../custom-http/http-client.service";

@Injectable({
  providedIn: 'root'
})
export class PassengerService {

  constructor(private http: HttpClientService) {}

  findAll() {
    return this.http.get(environment.apiUrl + "passengers");
  }
}
