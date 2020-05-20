package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс реализует ...
 *
 * @author Денис Висков
 * @version 1.0
 * @since 20.05.2020
 */
public class SqlRuPostParser implements Parser<Post, Document> {

    @Override
    public Post getData(Document someData) throws IOException {
        TimeConversion conversion = new TimeConversion();
        String url = someData.location();
        String description = getDescription(someData);
        LocalDateTime time = conversion.toDateChanger(List.of(getDate(someData))).get(0);
        String name = getName(someData);
        return new Post(url, name, description, time);
    }

    private String getDescription(Document document) {
        return document
                .select(".msgBody")
                .get(1)
                .text();
    }

    private String getDate(Document document) {
        return document.selectFirst(".msgFooter")
                .ownText()
                .replaceFirst("\\D+$", "");
    }

    private String getName(Document document) {
        return document.selectFirst(".messageHeader").ownText();
    }
}
