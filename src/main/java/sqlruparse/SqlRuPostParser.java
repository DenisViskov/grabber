package sqlruparse;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class is a post parse
 *
 * @author Денис Висков
 * @version 1.0
 * @since 20.05.2020
 */
public class SqlRuPostParser {

    /**
     * Converter
     */
    private final DataConverter converter;

    public SqlRuPostParser(DataConverter converter) {
        this.converter = converter;
    }

    /**
     * Method has realizes parsing one post by given document
     *
     * @param someData - someData
     * @return - Post
     * @throws IOException
     */
    public Post getData(String someData) throws IOException {
        TimeConversion conversion = new TimeConversion();
        Document document = (Document) converter.getData(someData);
        String url = document.location();
        String description = getDescription(document);
        LocalDateTime time = conversion.toDateChanger(List.of(getDate(document))).get(0);
        String name = getName(document);
        return new Post(url, name, description, time);
    }

    /**
     * Method getting description by post
     *
     * @param document - document
     * @return - description
     */
    private String getDescription(Document document) {
        return document
                .select(".msgBody")
                .get(1)
                .text();
    }

    /**
     * Method getting created time of post
     *
     * @param document - document
     * @return - Time
     */
    private String getDate(Document document) {
        return document.selectFirst(".msgFooter")
                .ownText()
                .replaceFirst("\\D+$", "");
    }

    /**
     * Method getting name of post
     *
     * @param document - document
     * @return - name
     */
    private String getName(Document document) {
        return document.selectFirst(".messageHeader")
                .ownText();
    }
}
