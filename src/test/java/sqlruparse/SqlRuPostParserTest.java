package sqlruparse;

import org.hamcrest.core.StringContains;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.hamcrest.core.StringContains.*;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class SqlRuPostParserTest {

    private static Document document;

    @BeforeClass
    public static void init() throws IOException {
        document = Jsoup.parse(SqlRuPostParser.class.getClassLoader()
                        .getResourceAsStream("PostTest.html"),
                "Cp1251",
                Paths.get("PostTest.html").toUri().toString());
    }

    @Test
    public void getDataTest() throws IOException {
        Post expected = new Post(Paths
                .get("PostTest.html")
                .toAbsolutePath()
                .toString()
                .replaceAll("\\\\", "/"),
                "Лиды BE/FE/senior cистемные аналитики/QA и DevOps, Москва, до 200т.",
                Files.readString(Paths.get("./src/test/resources/DescriptionPostTest.txt")),
                LocalDateTime.of(20,
                        5,
                        13,
                        21,
                        58));
        SqlRuPostParser parser = new SqlRuPostParser();
        Post out = parser.getData(document);
        assertThat(out.getName(), is(expected.getName()));
        assertThat(out.getUrl(), containsString(expected.getUrl()));
        assertThat(out.getCreated(), is(expected.getCreated()));
        assertThat(out.getDescription(), is(expected.getDescription()));
    }
}