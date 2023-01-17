import {Component, OnInit} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";
import {PassengerService} from "../../services/passengerService/passenger.service";
import {Router} from "@angular/router";
import {PaymentService} from "../../services/payment/payment.service";
import {CurrentPayingData, KeyPairDTO, PaymentData, PaymentStatus} from "../../model/payment/payment";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DrivingService} from "../../services/drivingService/driving.service";

@Component({
  selector: 'app-payment-page',
  templateUrl: './payment-page.component.html',
  styleUrls: ['./payment-page.component.scss']
})
export class PaymentPageComponent implements OnInit {

  hideSecretKey: boolean = true;

  paymentStatus: PaymentStatus;

  paymentData: PaymentData;

  userAlreadyHasPaymentData: boolean = false;

  useExistingChecked: boolean = false;

  apiLoading: boolean = true;

  form = new FormGroup({
    address: new FormControl('', [Validators.required]),
    secretKey: new FormControl('', [Validators.required]),
  });

  constructor(
    private snackBar: MatSnackBar,
    private passengerService: PassengerService,
    private paymentService: PaymentService,
    private drivingService: DrivingService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.passengerService.findCurrentDrivingWithLocations()
      .subscribe({
          next: (driving) => {
            if (driving) {
              if (driving.drivingState !== 'PAYMENT') {
                this.router.navigateByUrl('passenger/current-driving');
              } else {
                this.loadPaymentStatus();
                this.loadPaymentData();
                this.apiLoading = false;
              }
            } else {
              this.router.navigateByUrl('passenger/order-ride');
            }
          }
        }
      );
  }

  loadPaymentStatus() {
    this.paymentService.getPaymentStatus()
      .subscribe({
          next: (paymentStatus) => {
            this.paymentStatus = paymentStatus;
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

  loadPaymentData() {
    this.paymentService.getPaymentData()
      .subscribe({
          next: (paymentData) => {
            this.paymentData = paymentData;
            if (this.paymentData) {
              this.useExistingChecked = true;
              this.userAlreadyHasPaymentData = true;
            }
          },
        }
      );
  }
  onCancelClicked() {
    this.drivingService.rejectDrivingLinkedPassenger()
      .subscribe({
          next: (_) => {
            this.router.navigateByUrl('/passenger/order-now');
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            })
          }
        }
      );
  }

  onPayClicked() {
    let keyPair: KeyPairDTO = null;

    if (!this.useExistingChecked) {
      if (this.form.controls.address.invalid || this.form.controls.secretKey.invalid) {
        return;
      }
      keyPair = new KeyPairDTO(this.form.controls.secretKey.value, this.form.controls.address.value);
    }

    let payload = new CurrentPayingData(this.useExistingChecked, keyPair);
    this.apiLoading = true;

    this.paymentService.pay(payload)
      .subscribe({
          next: (_) => {
            this.apiLoading = false;
            this.paymentStatus.passengerApproved = true;
          },
          error: (error) => {
            this.snackBar.open(error.error.message, "ERROR", {
              duration: 5000,
            });
            this.apiLoading = false;
          }
        }
      );
  }
}
