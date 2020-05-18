package sqlruparse;

import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class SqlRuParserTest {

    private static final Document DOCUMENT = new Document("");

    @BeforeClass
    public static void init() {
        DOCUMENT.appendElement("td")
                .addClass("postslisttopic")
                .append("Важно: <a href=\"https://www.sql.ru/forum/484798/pravila-foruma\">Правила форума</a> \n"
                        + " <!--begin case_istopicclosed--> <span class=\"closedTopic\"> [закрыт]</span> \n"
                        + " <!--end case_istopicclosed--> &nbsp; <a class=\"newTopic\" "
                        + "href=\"https://www.sql.ru/forum/actualutils.aspx?action=gotonew&amp;tid=484798\">[new]</a>");
        DOCUMENT.appendElement("td")
                .addClass("altCol")
                .append("<td style=\"text-align:center\" class=\"altCol\">2 дек 19, 22:29</td>");
        DOCUMENT.appendElement("td")
                .addClass("postslisttopic")
                .append("Важно: <a href=\"https://www.sql.ru/forum/484798/pravila-foruma\">Правила форума</a> \n"
                        + " <!--begin case_istopicclosed--> <span class=\"closedTopic\"> [закрыт]</span> \n"
                        + " <!--end case_istopicclosed--> &nbsp; <a class=\"newTopic\" "
                        + "href=\"https://www.sql.ru/forum/actualutils.aspx?action=gotonew&amp;tid=484798\">[new]</a>");
        DOCUMENT.appendElement("td")
                .addClass("altCol")
                .append("<td style=\"text-align:center\" class=\"altCol\">сегодня, 22:29</td>");
        DOCUMENT.appendElement("td")
                .addClass("postslisttopic")
                .append("Важно: <a href=\"https://www.sql.ru/forum/484798/pravila-foruma\">Правила форума</a> \n"
                        + " <!--begin case_istopicclosed--> <span class=\"closedTopic\"> [закрыт]</span> \n"
                        + " <!--end case_istopicclosed--> &nbsp; <a class=\"newTopic\" "
                        + "href=\"https://www.sql.ru/forum/actualutils.aspx?action=gotonew&amp;tid=484798\">[new]</a>");
        DOCUMENT.appendElement("td")
                .addClass("altCol")
                .append("<td style=\"text-align:center\" class=\"altCol\">вчера, 22:29</td>");
    }

    @Test
    public void finalBuilderTest() {
        List<String> out = new SqlRuParser().finalBuilder(DOCUMENT);
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