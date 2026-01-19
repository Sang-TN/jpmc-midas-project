package com.jpmc.midascore.foundation;

public class Incentive {
    private float amount;

    // Standard-Konstruktor (wichtig für die API)
    public Incentive() {}

    public Incentive(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}