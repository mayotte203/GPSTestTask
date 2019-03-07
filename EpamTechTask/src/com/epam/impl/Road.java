package com.epam.impl;

public class Road {
    final String destination;
    final int length;
    final int costPerUnit;
    public int getCost()
    {
        return this.length * this.costPerUnit;
    }
    public Road(String destination, int length, int costPerUnit)
    {
        this.destination = destination;
        this.length = length;
        this.costPerUnit = costPerUnit;
    }
}
