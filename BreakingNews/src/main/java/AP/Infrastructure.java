package AP;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;

public class Infrastructure {

    private final String URL;
    private final String APIKEY;
    private final String JSONRESULT;
    private ArrayList<News> NewsList; // News class created successfully

    public Infrastructure(String APIKEY) {
        this.APIKEY = APIKEY;
        this.URL = "https://newsapi.org/v2/everything?q=tesla&from=" + LocalDate.now().minusDays(2) + "&sortBy=publishedAt&apiKey=";
        this.JSONRESULT = getInformation();
        this.NewsList = new ArrayList<>();
        parseInformation();
    }

    private String getInformation() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + APIKEY))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("HTTP error code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("!!Exception : " + e.getMessage());
        }
        return null;
    }

    private void parseInformation() {
        // convert the json string to array of json objects
        JsonObject jsonObj = JsonParser.parseString(JSONRESULT).getAsJsonObject();
        JsonArray articles = jsonObj.getAsJsonArray("articles");

        // gets the detailed information of each json element and fills in the array of News class.
        for (int i = 0; i < Math.min(articles.size(), 20); i++) {
            JsonObject article = articles.get(i).getAsJsonObject();

            String title = IsJsonValid(article, "title") ? article.get("title").getAsString() : "Article Number " + i;
            String description = IsJsonValid(article, "description") ? article.get("description").getAsString() : "No Description";
            String sourceName = IsJsonValid(article, "source") ? IsJsonValid(article.getAsJsonObject("source"), "name") ? article.getAsJsonObject("source").get("name").getAsString() : "Unknown" : "Unknown";
            String author = IsJsonValid(article, "author") ? article.get("author").getAsString() : "Unknown";
            String url = IsJsonValid(article, "url") ? article.get("url").getAsString() : "No URL";
            String publishedAt = IsJsonValid(article, "publishedAt") ? article.get("publishedAt").getAsString() : "Unknown";

            News CurrentNews = new News(title, description, sourceName, author, url, publishedAt);
            NewsList.add(CurrentNews);
        }
    }

    // this checker is needed to prevent any ERRORS.
    private boolean IsJsonValid(JsonObject obj, String str) {
        return obj.has(str) && !obj.get(str).isJsonNull();
    }

    public void displayNewsList() {
        for (int i = 0; i < GetArticlesCount(); i++) {
            System.out.println((i + 1) + ". " + NewsList.get(i).getTitle());
        }
    }

    public News GetSingleNews(int NewsIndex)
    {
        return NewsList.get(NewsIndex);
    }

    public int GetArticlesCount()
    {
        return NewsList.size();
    }
}
