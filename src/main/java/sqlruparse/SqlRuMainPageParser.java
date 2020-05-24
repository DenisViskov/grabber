package sqlruparse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class shows how to works with jsoup library
 *
 * @author Денис Висков
 * @version 1.0
 * @since 17.05.2020
 */
public class SqlRuMainPageParser implements Parse {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(SqlRuMainPageParser.class.getName());

    /**
     * Data converter
     */
    private final DataConverter dataConverter;

    public SqlRuMainPageParser(DataConverter dataConverter) {
        this.dataConverter = dataConverter;
    }

    public static void main(String[] args) throws IOException {
        //ProxyChanger.useThroughProxy();
        SqlRuMainPageParser parser = new SqlRuMainPageParser(new StringConverter());
        Document doc = (Document) parser.dataConverter.getData("https://www.sql.ru/forum/job-offers");
        for (String result : parser.parseGivenCountPages(5, "https://www.sql.ru/forum/job-offers")) {
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
                        || text.matches("^сегодня.+\\d$")
                        || text.matches("^вчера.+\\d$"))
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
    public List<String> finalBuilder(Document document) {
        TimeConversion timeConversion = new TimeConversion();
        List<String> urls = getUrls(document);
        List<String> names = getName(document);
        List<LocalDateTime> dates = timeConversion.toDateChanger(getDates(document));
        List<String> result = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            result.add(urls.get(i) + System.lineSeparator()
                    + names.get(i) + System.lineSeparator()
                    + dates.get(i).format(timeConversion.getDefaultFormatter())
                    + System.lineSeparator());
        }
        return result;
    }

    /**
     * Method parsing web pages by given count number
     *
     * @param number - count
     * @return - list of pages
     * @throws IOException
     */
    private List<String> parseGivenCountPages(int number, String url) throws IOException {
        List<String> result = new ArrayList<>();
        String page = url + "/";
        for (int i = 1; i != number; i++) {
            Document document = (Document) dataConverter.getData(page + i);
            result.addAll(finalBuilder(document));
        }
        return result;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        Document document = (Document) dataConverter.getData(link);
        SqlRuPostParser parser = new SqlRuPostParser(dataConverter);
        List<String> urls = getUrls(document);
        List<Post> result = new ArrayList<>();
        for (String page : urls) {
            Post post = parser.getData(page);
            result.add(post);
        }
        return result;
    }

    @Override
    public Post detail(String link) throws IOException {
        SqlRuPostParser parser = new SqlRuPostParser(dataConverter);
        return parser.getData(link);
    }
}
