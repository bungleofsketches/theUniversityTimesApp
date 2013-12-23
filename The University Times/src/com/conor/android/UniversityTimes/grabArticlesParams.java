package com.conor.android.UniversityTimes;

public class grabArticlesParams {
    String category;
    int amount, before_id, first_id;

    grabArticlesParams(int amount, String category, int before_id, int first_id) {
        this.amount = amount;
        this.category = category;
        this.before_id = before_id;
        this.first_id = first_id;
    }

}