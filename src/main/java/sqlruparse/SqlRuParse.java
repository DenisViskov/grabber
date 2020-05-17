package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        SqlRuParse parse = new SqlRuParse();
        System.out.println(parse.finalBuilder(doc));
    }

    private List<String> getUrls(Document document) {
        return document.select(".postslisttopic")
                .stream()
                .map(element -> element.attr("href"))
                .collect(Collectors.toList());
    }

    private List<String> getDates(Document document) {
        return document.select(".altCol")
                .stream()
                .map(Element::text)
                .filter(text -> text.matches("^\\d+.+\\d$"))
                .collect(Collectors.toList());
    }

    private List<String> getName(Document document) {
        return document.select(".postslisttopic")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
    }

    private List<String> finalBuilder(Document document) {
        List<String> urls = getUrls(document);
        List<String> names = getName(document);
        List<String> dates = getDates(document);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            result.add(urls.get(i) + System.lineSeparator()
                    + names.get(i) + System.lineSeparator()
                    + dates.get(i) + System.lineSeparator());
        }
        return result;
    }
}
