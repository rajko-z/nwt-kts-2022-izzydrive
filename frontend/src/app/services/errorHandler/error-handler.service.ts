import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  /*key: custom code from backend, value: html field name property */
  customErrorCode = {
    1001: "email",
    1002: "lastName",
    1003: "password",
    1004: "repeatedPassword",
    1005: "phoneNumber",
    1006: "other",
    1007: "firstName"
}

  constructor() { }
}
