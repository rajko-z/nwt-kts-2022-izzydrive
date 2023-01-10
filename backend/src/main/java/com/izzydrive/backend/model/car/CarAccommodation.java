package com.izzydrive.backend.model.car;

public class CarAccommodation {
    private boolean food;
    private boolean pet;
    private boolean baggage;
    private boolean baby;

    public CarAccommodation(boolean food, boolean pet, boolean baggage, boolean baby) {
        this.food = food;
        this.pet = pet;
        this.baggage = baggage;
        this.baby = baby;
    }

    public int returnNumOfSimilarGoods(CarAccommodation other) {
        int num = 0;
        num = (this.food && other.food) ? num + 1 : num;
        num = (this.pet && other.pet) ? num + 1 : num;
        num = (this.baggage && other.baggage) ? num + 1 : num;
        num = (this.baby && other.baby) ? num + 1 : num;
        return num;
    }

    public CarAccommodation(){

    }

    public boolean isFood() {
        return food;
    }

    public boolean isPet() {
        return pet;
    }

    public boolean isBaggage() {
        return baggage;
    }

    public boolean isBaby() {
        return baby;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public void setPet(boolean pet) {
        this.pet = pet;
    }

    public void setBaggage(boolean baggage) {
        this.baggage = baggage;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }
}
