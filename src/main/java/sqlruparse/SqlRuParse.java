package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Class shows how to works with jsoup library
 *
 * @author Денис Висков
 * @version 1.0
 * @since 17.05.2020
 */
public class SqlRuParse {

    private final Map<String, Integer> month = new HashMap<>();

    public SqlRuParse() {
        month.put("янв", 1);
        month.put("фев", 2);
        month.put("мар", 3);
        month.put("апр", 4);
        month.put("май", 5);
        month.put("июн", 6);
        month.put("июл", 7);
        month.put("авг", 8);
        month.put("сен", 9);
        month.put("окт", 10);
        month.put("ноя", 11);
        month.put("дек", 12);
    }

    public static void main(String[] args) throws IOException {
        useThroughProxy();
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        SqlRuParse parse = new SqlRuParse();
        parse.toDateChanger(parse.getDates(doc));
        for (String result : parse.finalBuilder(doc)) {
            System.out.println(result);
        }
    }

    /**
     * Method getting urls from HTML page by postslisttopic class tag
     *
     * @param document - document HTML
     * @return - List of urls
     */
    private List<String> getUrls(Document document) {
        return document.select(".postslisttopic")
                .stream()
                .map(element -> element.child(0).attr("href"))
                .collect(Collectors.toList());
    }

    /**
     * Method getting dates from HTML page by altCol tag
     *
     * @param document - document
     * @return - List of dates
     */
    private List<String> getDates(Document document) {
        return document.select(".altCol")
                .stream()
                .map(Element::text)
                .filter(text -> text.matches("^\\d+.+\\d$")
                        || text.matches("^сегодня.+\\d$"))
                .collect(Collectors.toList());
    }

    /**
     * Method getting names of jobs by postslisttopic tag on HTML page
     *
     * @param document - document
     * @return - list of names
     */
    private List<String> getName(Document document) {
        return document.select(".postslisttopic")
                .stream()
                .map(element -> element.child(0).text())
                .collect(Collectors.toList());
    }

    /**
     * Method is a builder finally list of urls,names and dates
     *
     * @param document - document
     * @return - List of urls/names/dates from HTML
     */
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

    private List<LocalDateTime> toDateChanger(List<String> dates) {
        return dates.stream()
                .map(line -> line.matches("^\\d+.+") ? ifLineBeginWithNumber(line)
                        : ifLineBeginWithWord(line)
                ).collect(Collectors.toList());
    }

    private LocalDateTime ifLineBeginWithNumber(String line) {
        String[] split = line.split(" ");
        int year = Integer.parseInt(split[2].replaceFirst(",", ""));
        int day = Integer.parseInt(split[0]);
        Month month = Month.of(this.month.get(split[1]));
        int hour = Integer.valueOf(split[3].split(":")[0]);
        int minute = Integer.valueOf(split[3].split(":")[1]);
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute);
        return time;
    }

    private LocalDateTime ifLineBeginWithWord(String line) {
        String[] split = line.split(" ");
        int year = Integer.parseInt(split[2].replaceFirst(",", ""));
        int day = split[0].contains("сегодня") ? LocalDateTime.now().getDayOfMonth()
                : LocalDateTime.now().getDayOfMonth() - 1;
        Month month = Month.of(this.month.get(split[1]));
        int hour = Integer.valueOf(split[3].split(":")[0]);
        int minute = Integer.valueOf(split[3].split(":")[1]);
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute);
        return time;
    }

    private static void useThroughProxy() {
        System.setProperty("https.proxyHost", "192.168.111.102");
        System.setProperty("https.proxyPort", "3128");
    }
}
