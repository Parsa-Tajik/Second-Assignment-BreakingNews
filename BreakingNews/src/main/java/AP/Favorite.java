package AP;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.util.ArrayList;

public class Favorite {
    private ArrayList<News> favoriteNews;
    private String filePath;

    public Favorite()
    {
        filePath = "BreakingNews/src/data/favoriteArticles.json";
        favoriteNews = new ArrayList<>();
        readFile();
    }

    // this one reads the Json file at the beginning, converts to News objects and fills in the favoriteNews array.
    private void readFile()
    {
        try (FileReader reader = new FileReader(filePath)) {
            JsonArray favoriteJsonArr = JsonParser.parseReader(reader).getAsJsonArray();

            for (int i = 0; i < favoriteJsonArr.size(); i++) {
                JsonObject favoriteJsonObj = favoriteJsonArr.get(i).getAsJsonObject();

                String title = favoriteJsonObj.get("title").getAsString();
                String description = favoriteJsonObj.get("description").getAsString();
                String sourceName = favoriteJsonObj.get("sourceName").getAsString();
                String author = favoriteJsonObj.get("author").getAsString();
                String url = favoriteJsonObj.get("url").getAsString();
                String publishedAt = favoriteJsonObj.get("publishedAt").getAsString();

                favoriteNews.add(new News(title, description, sourceName, author, url, publishedAt));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void add(News news)
    {
        favoriteNews.add(news);
        updateFile();
    }

    public void remove(News news)
    {
        favoriteNews.remove(news);
        updateFile();
    }

    // this one converts the News array into Json array and writes it on the file.
    private void updateFile()
    {
        JsonArray favoriteJsonArr = new JsonArray();
        for (News news : favoriteNews) {
            JsonObject newsJson = new JsonObject();
            newsJson.addProperty("title", news.getTitle());
            newsJson.addProperty("description", news.getDescription());
            newsJson.addProperty("sourceName", news.getSourceName());
            newsJson.addProperty("author", news.getAuthor());
            newsJson.addProperty("url", news.getUrl());
            newsJson.addProperty("publishedAt", news.getPublishedAt());

            favoriteJsonArr.add(newsJson);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(favoriteJsonArr.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayFavoriteList()
    {
        int i = 1;
        for (News news : favoriteNews) {
            System.out.println(i + ". " + news.getTitle());
            i++;
        }
    }

    public News getSingleNews(int index)
    {
        return favoriteNews.get(index);
    }

    public int count()
    {
        return favoriteNews.size();
    }

    public boolean isFavorite(News news)
    {
        return favoriteNews.contains(news);
    }
}
