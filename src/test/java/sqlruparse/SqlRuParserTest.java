package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class SqlRuParserTest {

    private static Document document;

    @BeforeClass
    public static void init() throws IOException {
        document = Jsoup.parse(SqlRuParserTest.class.getClassLoader()
                        .getResourceAsStream("Test.html"),
                "windows-1251",
                Paths.get("Test.html").toUri().toString());
    }

    @Test
    public void finalBuilderTest() {
        List<String> out = new SqlRuParser().finalBuilder(document);
        TimeConversion conversion = new TimeConversion();
        String post = "https://www.sql.ru/forum/484798/pravila-foruma"
                + System.lineSeparator()
                + "Правила форума"
                + System.lineSeparator();
        List<String> expected = List.of(post
                        + LocalDateTime.of(2019, 12, 2, 22, 29)
                        .format(conversion.getDefaultFormatter())
                        + System.lineSeparator(),
                post
                        + LocalDateTime.of(LocalDateTime.now().getYear(),
                        LocalDateTime.now().getMonth(),
                        LocalDateTime.now().getDayOfMonth(), 22, 29)
                        .format(conversion.getDefaultFormatter())
                        + System.lineSeparator(),
                post
                        + LocalDateTime.of(LocalDateTime.now().getYear(),
                        LocalDateTime.now().getMonth(),
                        LocalDateTime.now().getDayOfMonth() - 1, 22, 29)
                        .format(conversion.getDefaultFormatter())
                        + System.lineSeparator()
        );
        assertThat(expected, is(out));
    }

    @Test
    public void getDataTest() throws IOException {
        SqlRuParser parser = new SqlRuParser();
        Document expected = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Document out = parser.getData("https://www.sql.ru/forum/job-offers");
        assertThat(expected.head().text(), is(out.head().text()));
    }

    @Test(expected = IOException.class)
    public void getDataThrowIOException() throws IOException {
        SqlRuParser parser = new SqlRuParser();
        ProxyChanger.useThroughProxy();
        Document out = parser.getData("https://www.sql.ru/forum/job-offers");
        ProxyChanger.useThroughDefaultNetwork();
    }


    @Test
    public void listTest() throws IOException {
        Post post = new Post("https://www.sql.ru/forum/484798/pravila-foruma",
                "Правила форума",
                "blabla",
                LocalDateTime.of(2019,
                        12,
                        2,
                        22,
                        29));
        List<Post> out = new SqlRuParser().list(document.location());
        assertThat(List.of(post.getName(),
                post.getUrl(),
                post.getCreated()), is(List.of(out.get(0).getName(),
                out.get(0).getUrl(),
                out.get(0).getCreated())));
        assertThat(List.of(post.getName(),
                post.getUrl(),
                post.getCreated().
                        withDayOfMonth(LocalDateTime.now()
                                .getDayOfMonth())),
                is(List.of(out.get(0).getName(),
                        out.get(0).getUrl(),
                        out.get(0).getCreated())));
        assertThat(List.of(post.getName(),
                post.getUrl(),
                post.getCreated().
                        withDayOfMonth(LocalDateTime.now()
                                .getDayOfMonth() - 1)),
                is(List.of(out.get(0).getName(),
                        out.get(0).getUrl(),
                        out.get(0).getCreated())));
    }

    @Test
    public void detailTest() {
    }
}