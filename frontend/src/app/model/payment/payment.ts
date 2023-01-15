export class PaymentStatus {
  passengerApproved: boolean;
  waitingForOthers: boolean;
  sessionExpired: boolean;
}

export class PaymentData {
  secretKey: string;
  ethAddress: string;

  constructor(secretKey: string, ethAddress: string) {
    this.secretKey = secretKey;
    this.ethAddress = ethAddress;
  }
}
