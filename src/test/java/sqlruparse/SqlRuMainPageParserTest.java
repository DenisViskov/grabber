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

public class SqlRuMainPageParserTest {

    private static Document document;

    @BeforeClass
    public static void init() throws IOException {
        document = Jsoup.parse(SqlRuMainPageParserTest.class.getClassLoader()
                        .getResourceAsStream("Test.html"),
                "windows-1251",
                Paths.get("Test.html").toUri().toString());
    }

    @Test
    public void finalBuilderTest() {
        List<String> out = new SqlRuMainPageParser(new StringConverter()).finalBuilder(document);
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
}