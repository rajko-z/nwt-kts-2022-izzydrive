import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PaymentService} from "../../services/payment/payment.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PaymentData} from "../../model/payment/payment";
import { ResponseMessageService } from 'src/app/services/response-message/response-message.service';

@Component({
  selector: 'app-paying-info',
  templateUrl: './paying-info.component.html',
  styleUrls: ['./paying-info.component.scss']
})
export class PayingInfoComponent implements OnInit {

  hideSecretKey: boolean = true;

  form = new FormGroup({
    address: new FormControl('', [Validators.required]),
    secretKey: new FormControl('', [Validators.required]),
  });
  constructor(
    private paymentService: PaymentService,
    private snackBar: MatSnackBar,
    private responseMessage: ResponseMessageService) { }

  ngOnInit(): void {
    this.paymentService.getPaymentData()
      .subscribe({
          next: (response) => {
            if (response !== undefined && response !== null) {
              this.form.patchValue({
                address: response.ethAddress,
                secretKey: response.secretKey
              });
            }
          },
          error: (error) => {
            this.responseMessage.openErrorMessage(error.error.message)
          }
        }
      );
  }

  onSubmit() {
    const ethAddress = this.form.controls.address.value;
    const secretKey = this.form.controls.secretKey.value;
    const payload = new PaymentData(secretKey, ethAddress);

    this.paymentService.savePaymentData(payload)
      .subscribe({
          next: (response) => {
            this.responseMessage.openSuccessMessage(response.text)
          },
          error: (error) => {
            this.responseMessage.openErrorMessage(error.error.message)
          }
        }
      );
  }

}
