import {Injectable} from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";
import {PaymentStatus} from "../../model/payment/payment";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private httpClientService: HttpClientService) {
  }

  getPaymentStatus() {
    return this.httpClientService.getT<PaymentStatus>(environment.apiUrl + 'payment/status');
  }
}
