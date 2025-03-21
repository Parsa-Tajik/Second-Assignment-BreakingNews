package AP;

import java.util.Scanner;

public class Main {
    private static Infrastructure allNews;
    private static Favorite favoriteNews;
    private static int articlesCount;
    private static int favoriteCount;
    private static Scanner sc;

    public static void main(String[] args) {
        allNews = new Infrastructure("f81c9919d7a84ac88901281c2726f189");
        articlesCount = allNews.GetArticlesCount();

        // if there was a problem in the website and no news was found, we do not continue
        if (articlesCount == 0)
        {
            System.out.println("Sorry, No News Found! try again in a while...");
            return;
        }

        favoriteNews = new Favorite();
        favoriteCount = favoriteNews.count();

        sc = new Scanner(System.in);

        boolean exit = false;
        while (!exit)
        {
            clear();

            System.out.println("1. Latest News");
            System.out.println("2. Your Favorite News");
            System.out.println("3. Exit");
            System.out.print("Please enter the option: ");
            int choice = sc.nextInt();
            while (choice < 1 || choice > 3) {
                System.out.println("Please enter a valid option: ");
                choice = sc.nextInt();
            }

            switch (choice) {
                case 1:
                    showLatestNews();
                    break;
                case 2:
                    showFavoriteList();
                    break;
                case 3:
                    exit = true;
            }
        }
    }

    private static void showLatestNews() {
        while (true) {
            clear();
            allNews.displayNewsList();

            System.out.println("\nplease enter the number of the article you wish to read, or enter 0 to return to the main menu.");
            int choice = sc.nextInt();
            while (choice < 0 || choice > articlesCount)
            {
                System.out.println("Please enter a number between 0 and " + articlesCount + ": ");
                choice = sc.nextInt();
            }

            if (choice == 0) {
                break;
            }else {
                showSingleNews(allNews.GetSingleNews(choice - 1));
            }
        }
    }

    private static void showFavoriteList() {
        if (favoriteCount == 0)
        {
            int choice;
            do {
                System.out.println("your Favorite list is empty, please enter 0 to return to the main menu.");
                choice = sc.nextInt();
            }while (choice != 0);

            return;
        }

        while (true) {
            clear();
            favoriteNews.displayFavoriteList();

            System.out.println("\nplease enter the number of the article you wish to read, or enter 0 to return to the main menu.");
            int choice = sc.nextInt();
            while (choice < 0 || choice > favoriteCount)
            {
                System.out.println("Please enter a number between 0 and " + favoriteCount + ": ");
                choice = sc.nextInt();
            }

            if (choice == 0) {
                break;
            }else {
                showSingleNews(favoriteNews.getSingleNews(choice - 1));
            }
        }
    }

    private static void showSingleNews(News news) {
        clear();
        System.out.println(news);
        boolean isFavorite;
        int choice;
        do {
            isFavorite = favoriteNews.isFavorite(news);

            if (isFavorite)
            {
                System.out.println("\nplease enter 1 to remove this article from your favorite list,\nor enter 0 to return to list:");
            }else {
                System.out.println("\nplease enter 1 to add this article to your favorite list,\nor enter 0 to return to list:");
            }
            choice = sc.nextInt();

            if (choice == 1) {
                if (isFavorite)
                {
                    favoriteNews.remove(news);
                }else {
                    favoriteNews.add(news);
                }
                favoriteCount = favoriteNews.count();
            }
        } while (choice != 0);
    }

    private static void clear() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}