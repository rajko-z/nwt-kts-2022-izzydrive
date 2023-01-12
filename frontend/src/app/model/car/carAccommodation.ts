export class CarAccommodation {
  food : boolean;
  pet : boolean;
  baggage: boolean;
  baby: boolean;

  constructor(food?: boolean, pet?: boolean, baggage?: boolean, baby?: boolean) {
    this.food = food;
    this.pet = pet;
    this.baggage = baggage;
    this.baby = baby;
  }
}
