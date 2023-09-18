package template.news.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import template.news.model.News;

public class GlobalVariable {

    private static List<News> allNews = new ArrayList<>();
    private static List<News> newsPolitics = new ArrayList<>();
    private static List<News> newsEntertainment = new ArrayList<>();
    private static List<News> newsScience = new ArrayList<>();
    private static List<News> newsSport = new ArrayList<>();
    private static List<News> newsBusiness = new ArrayList<>();
    private static List<News> newsTechnology = new ArrayList<>();
    private static List<News> saved = new ArrayList<>();

    public static void init(Context context) {
        allNews = Constant.getAllNews(context);
        newsPolitics = Constant.getNewsPolitics(context);
        newsEntertainment = Constant.getNewsEntertainment(context);
        newsScience = Constant.getNewsScience(context);
        newsSport = Constant.getNewsSport(context);
        newsBusiness = Constant.getNewsBusiness(context);
        newsTechnology = Constant.getNewsTechnology(context);
    }

    public static List<News> getAllNews() {
        return allNews;
    }

    // news by channel

    public static List<News> getNewsPolitics() {
        return newsPolitics;
    }

    public static List<News> getNewsEntertainment() {
        return newsEntertainment;
    }

    public static List<News> getNewsScience() {
        return newsScience;
    }

    public static List<News> getNewsSport() {
        return newsSport;
    }

    public static List<News> getNewsBusiness() {
        return newsBusiness;
    }

    public static List<News> getNewsTechnology() {
        return newsTechnology;
    }

    // news by category

    public static List<News> getNewsLatest() {
        return allNews.subList(0, 7);
    }

    public static List<News> getNewsTrending() {
        return allNews.subList(7, 12);
    }

    public static List<News> getNewsHighlight() {
        return allNews.subList(12, 18);
    }

    public static List<News> getNewsPopular() {
        return allNews.subList(18, 24);
    }

    public static List<News> getNewsMostview() {
        return allNews.subList(24, 30);
    }

    public static List<News> getSaved() {
        return saved;
    }

    public static void addSaved(News s) {
        saved.add(s);
    }

    public static void removeSaved(News s) {
        for (int i = 0; i < saved.size(); i++) {
            if (saved.get(i).getTitle().equalsIgnoreCase(s.getTitle())) {
                saved.remove(i);
                break;
            }
        }
    }

    public static boolean isSaved(News s) {
        for (News a : saved) {
            if (a.getTitle().equalsIgnoreCase(s.getTitle())) {
                return true;
            }
        }
        return false;
    }

}
