package com.conor.android.UniversityTimes;


import java.util.ArrayList;

//this class is not finished.
public class RandomArticleGenerator {

    public static final int NUM_CHOICES = 3;
    public static final String[] headings =
            {"This is the end of overpaid workers!",
                    "Trinity College fall from the top 100 rankings.",
                    "Pedestrians outside Trinity are having trouble."};
    public static final String[] iurls =
            {"http://static.ddmcdn.com/gif/protest-8.jpg",
                    "http://www.embassyces.com/images_v2/content/Melbas-5th-September-2008.jpg",
                    "http://www.naturopathy.ie/blog/wp-content/uploads/2008/06/cnmie_grads1.jpg"};

    public RandomArticleGenerator() {
    }

    public Article getRandomArticle(int index) {
        Article randomArticle = new Article();
        int cursor = (int) (((Math.random() * 10) + index) % NUM_CHOICES);
        randomArticle.setHeading(headings[cursor]);
        randomArticle.setBody("AMD has refreshed its lineup of eight-core FX chips in what sounds like some straightforward overclocking of last year's products. The FX-9590 claims a clock speed of 5GHz in turbo mode, making it the \"world's first commercially available 5GHz CPU processor,\" while the FX-9370 lags slightly behind at 4.7GHz, as compared to the 4.2GHz top speed of the current FX-8350. Both new CPUs are based on the familiar Piledriver core, which has a reputation for being relatively cheap and easily overclockable (honestly, the 5GHz barrier was obliterated long ago), but far behind an Intel Core i5 in terms of all-around computing. This is especially true since the launch of Haswell, which largely avoided clock speed increases in favor of architectural tweaks that didn't compromise efficiency. Maingear plans to pick up the 5GHz part for use in a gaming system coming this summer, but there's no word yet on pricing or even general availability for DIY upgraders. Now, we're just speculating, but with AMD increasingly focused on APUs, it's possible that today's chips will represent the FX's lap of glory.");
        randomArticle.setImageurl(iurls[cursor]);
        randomArticle.setUrl("21718");

        return randomArticle;
    }

    public ArrayList getRandomArticles(int count) {
        ArrayList articles = new ArrayList<Article>();
        int index = 11;
        while (count > 0) {
            index++;
            articles.add(getRandomArticle(index));
            count--;
        }
        return articles;
    }

}
