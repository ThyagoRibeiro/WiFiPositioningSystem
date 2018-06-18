package com.example.thyag.poiwifidata.model;

public class ThingProbability {
    private Thing thing;
    private double probability;

    public ThingProbability(Thing thing, double probability) {
        this.thing = thing;
        this.probability = probability;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }
}
