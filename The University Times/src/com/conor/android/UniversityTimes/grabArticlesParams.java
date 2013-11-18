package com.conor.android.UniversityTimes;

public class grabArticlesParams {
    int amount;
    String category;
    int before_id;

    grabArticlesParams(int amount, String category, int before_id) {
        this.amount = amount;
        this.category = category;
        this.before_id = before_id;
    }

}