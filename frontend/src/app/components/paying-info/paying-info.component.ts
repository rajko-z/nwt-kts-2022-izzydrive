import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PaymentService} from "../../services/payment/payment.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PaymentData} from "../../model/payment/payment";

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
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.paymentService.getPaymentData()
      .subscribe({
          next: (response) => {
            console.log(response)
            if (response !== undefined && response !== null) {
              this.form.patchValue({
                address: response.ethAddress,
                secretKey: response.secretKey
              });
            }
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
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
            this.snackBar.open(response.text, "OK", {
              duration: 5000,
            })
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

}
