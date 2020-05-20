package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

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
       // Post expected = new Post("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t",
               // "Лиды BE/FE/senior cистемные аналитики/QA и DevOps, Москва, до 200т.", "")
        SqlRuPostParser parser = new SqlRuPostParser();
        parser.getData(document);
    }
}