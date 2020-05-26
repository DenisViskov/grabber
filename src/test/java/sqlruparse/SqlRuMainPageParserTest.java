package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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


    @Test
    public void listTest() throws IOException {
        DataConverter converter = Mockito.mock(StringConverter.class);
        SqlRuMainPageParser parser = new SqlRuMainPageParser(converter);
        Document document = Jsoup
                .parse(new File(Paths.get("./src/test/resources/PostTest.html")
                        .toAbsolutePath().toString()), "windows-1251");
        Mockito.when(converter.getData(Mockito.any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                if (args[0].toString().matches("http.+")) {
                    return document;
                }
                return invocationOnMock.callRealMethod();
            }
        });
        List<Post> out = parser.list(Paths.get("./src/test/resources/Test.html").toAbsolutePath().toString());
        assertThat(3, is(out.size()));
    }

    @Test
    public void detailTest() throws IOException {
        DataConverter converter = Mockito.mock(DataConverter.class);
        Document document = Jsoup
                .parse(new File(Paths.get("./src/test/resources/PostTest.html")
                        .toAbsolutePath().toString()), "windows-1251");
        Mockito.when(converter.getData(Mockito.any())).thenReturn(document);
        SqlRuMainPageParser parser = new SqlRuMainPageParser(converter);
        Post expected = new Post(document.location(),
                "Лиды BE/FE/senior cистемные аналитики/QA и DevOps, Москва, до 200т.",
                Files.readString(Paths.get("./src/test/resources/DescriptionPostTest.txt")),
                LocalDateTime.of(20,
                        5,
                        13,
                        21,
                        58));
        Post out = parser.detail(expected.getUrl());
        assertThat(out, is(expected));
    }

    @Test
    public void filterTest() {
        List<Post> input = List.of(new Post("",
                        "java,sdggasdf,sdfasf",
                        "",
                        LocalDateTime.now()),
                new Post("",
                        "c++,sfgsh,sdfgadgfhj",
                        "", LocalDateTime.now()),
                new Post("",
                        "Python,asfgdfg",
                        "",
                        LocalDateTime.now()));
        List<Post> expected = List.of(new Post("",
                "java,sdggasdf,sdfasf",
                "",
                LocalDateTime.now()));
        SqlRuMainPageParser parser = new SqlRuMainPageParser(null);
        List<Post> out = parser.filter(post -> post.getName().contains("java"), input);
        assertThat(out.toString(), is(expected.toString()));
    }
}