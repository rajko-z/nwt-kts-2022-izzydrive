import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-payment-form',
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.scss']
})
export class PaymentFormComponent implements OnInit {

  @Output() formData = new EventEmitter<FormGroup>();
  @Input() rideForm?: FormGroup;

  constructor() {
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.formData.emit();
  }

}
