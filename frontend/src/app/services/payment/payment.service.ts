import {Injectable} from '@angular/core';
import {HttpClientService} from "../custom-http/http-client.service";
import {environment} from "../../../environments/environment";
import {CurrentPayingData, PaymentData, PaymentStatus} from "../../model/payment/payment";
import {TextResponse} from "../../model/response/textresponse";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private httpClientService: HttpClientService) {
  }

  getPaymentStatus() {
    return this.httpClientService.getT<PaymentStatus>(environment.apiUrl + 'payment/status');
  }

  getPaymentData() {
    return this.httpClientService.getT<PaymentData>(environment.apiUrl + 'payment/payment-data');
  }

  savePaymentData(payload: PaymentData) {
    return this.httpClientService.putT<TextResponse>(environment.apiUrl + 'payment/payment-data', payload);
  }

  pay(payload: CurrentPayingData) {
    return this.httpClientService.putT<TextResponse>(environment.apiUrl + 'payment/pay', payload);
  }
}
