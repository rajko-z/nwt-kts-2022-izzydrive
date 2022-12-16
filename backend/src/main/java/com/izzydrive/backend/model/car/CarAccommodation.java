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
