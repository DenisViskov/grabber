package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class StringConverterTest {

    @Test
    public void getDataTest() throws IOException {
        StringConverter converter = new StringConverter();
        Document expected = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Document out = converter.getData("https://www.sql.ru/forum/job-offers");
        assertThat(expected.head().text(), is(out.head().text()));
    }

    @Test(expected = IOException.class)
    public void getDataThrowIOException() throws IOException {
        StringConverter converter = new StringConverter();
        ProxyChanger.useThroughProxy();
        Document out = converter.getData("https://www.sql.ru/forum/job-offers");
        ProxyChanger.useThroughDefaultNetwork();
    }

    @Test
    public void getDataFromFileTest() throws IOException {
        StringConverter converter = new StringConverter();
        Document expected = Jsoup.parse(new File(Paths.get("./src/test/resources/Test.html")
                        .toAbsolutePath()
                        .toString()),
                "windows-1251");
        Document out = converter.getData("./src/test/resources/Test.html");
        assertThat(expected.head().text(), is(out.head().text()));
    }

    @Test(expected = IOException.class)
    public void getDataFromFileExceptionTest() throws IOException {
        StringConverter converter = new StringConverter();
        Document out = converter.getData("adfsgdzsfg");
    }
}