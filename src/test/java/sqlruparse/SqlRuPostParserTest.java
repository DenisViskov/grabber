package sqlruparse;

import org.hamcrest.core.StringContains;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

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
        DataConverter converter = Mockito.mock(DataConverter.class);
        Mockito.when(converter.getData(Mockito.any())).thenReturn(document);
        Post expected = new Post(document.location(),
                "Лиды BE/FE/senior cистемные аналитики/QA и DevOps, Москва, до 200т.",
                Files.readString(Paths.get("./src/test/resources/DescriptionPostTest.txt")),
                LocalDateTime.of(20,
                        5,
                        13,
                        21,
                        58));
        SqlRuPostParser parser = new SqlRuPostParser(converter);
        Post out = parser.getData("./src/test/resources/DescriptionPostTest.txt");
        assertThat(out, is(expected));
    }
}