export class PaymentStatus {
  passengerApproved: boolean;
  waitingForOthers: boolean;
  sessionExpired: boolean;
  priceInRSD: number;
  priceInETH: number;
}

export class PaymentData {
  secretKey: string;
  ethAddress: string;

  constructor(secretKey: string, ethAddress: string) {
    this.secretKey = secretKey;
    this.ethAddress = ethAddress;
  }
}

export class CurrentPayingData {
  usingExistingPayingInfo: boolean;
  onceTimeKeyPair: KeyPairDTO;

  constructor(usingExistingPayingInfo: boolean, onceTimeKeyPair: KeyPairDTO) {
    this.usingExistingPayingInfo = usingExistingPayingInfo;
    this.onceTimeKeyPair = onceTimeKeyPair;
  }
}

export class KeyPairDTO {
  secretKey: string;
  ethAddress: string;

  constructor(secretKey: string, ethAddress: string) {
    this.secretKey = secretKey;
    this.ethAddress = ethAddress;
  }
}
