package com.apmato.evolveme;

/**
 * Created by hyuntae on 17/10/16.
 */
public class StatisticsData {
    int daysRemaining;
    float initialWeight;
    float weight;
    float percentage;
    int achievements;

    public StatisticsData(){
        daysRemaining = -1;
        initialWeight = -1;
        weight = -1;
        percentage = -1;
        achievements = -1;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(int daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public float getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(float initialWeight) {
        this.initialWeight = initialWeight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getAchivements() {
        return achievements;
    }

    public void setAchievements(int achievements) {
        this.achievements = achievements;
    }


}
