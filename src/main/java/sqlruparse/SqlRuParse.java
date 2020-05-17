package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс реализует ...
 *
 * @author Денис Висков
 * @version 1.0
 * @since 17.05.2020
 */
public class SqlRuParse {

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        System.out.println(getUrlNameAndDate(doc));
    }

    private static String getUrlNameAndDate(Document document) {
        StringJoiner result = new StringJoiner(System.lineSeparator());
        AtomicInteger increment = new AtomicInteger();
        List<String> dates = getDate(document);
        document.select(".postslisttopic")
                .stream()
                .forEach(element -> {
                    result.add(element.child(0).attr("href"));
                    result.add(element.child(0).text());
                    result.add(dates.get(increment.getAndIncrement()));
                    result.add(System.lineSeparator());
                });
        return result.toString();
    }

    private static List<String> getDate(Document document) {
        List<String> result = new ArrayList<>();
        Elements row = document.select(".altCol");
        for (int i = 0; i < row.size(); i++) {
            if (i % 2 != 0) {
                result.add(row.get(i).text());
            }
        }
        return result;
    }
}
