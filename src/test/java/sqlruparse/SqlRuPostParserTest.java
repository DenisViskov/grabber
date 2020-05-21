package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class SqlRuPostParserTest {

    private static Document document;

    @BeforeClass
    public static void init() throws IOException {
        document = Jsoup.parse(SqlRuPostParser.class.getClassLoader()
                        .getResourceAsStream("SqlRuPostParserTest.html"),
                "windows-1251",
                Paths.get("SqlRuPostParserTest.html").toUri().toString());
    }

    @Test
    public void getDataTest() throws IOException {
        Post expected = new Post("file:///C:/projects/job4j_grabber/SqlRuPostParserTest.html",
                "Лиды BE/FE/senior cистемные аналитики/QA и DevOps, Москва, до 200т.",
                Files.readString(Paths.get("./src/test/resources/DescriptionPostTest.txt")),
                LocalDateTime.of(20,
                        5,
                        20,
                        21,
                        58));
        SqlRuPostParser parser = new SqlRuPostParser();
        Post out = parser.getData(document);
        assertThat(expected.toString(), is(out.toString()));
    }
}