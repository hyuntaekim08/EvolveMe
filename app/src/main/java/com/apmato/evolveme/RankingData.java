package com.apmato.evolveme;

/**
 * Created by hyuntae on 19/10/16.
 */
public class RankingData {
    String name;
    float percent;
    boolean verifiedByReferee;
    boolean isSelf;

    public RankingData(){
        name = "";
        percent = 0;
        verifiedByReferee = false;
        isSelf = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public boolean isVerifiedByReferee() {
        return verifiedByReferee;
    }

    public void setVerifiedByReferee(boolean verifiedByReferee) {
        this.verifiedByReferee = verifiedByReferee;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }
}
