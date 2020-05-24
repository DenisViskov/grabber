package sqlruparse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;

/**
 * Class works with String source
 *
 * @author Денис Висков
 * @version 1.0
 * @since 24.05.2020
 */
public class StringConverter implements DataConverter<Document, String> {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(StringConverter.class);

    /**
     * Method returns data as a Document jsoup library
     *
     * @param someData - someData
     * @return - Document
     * @throws IOException
     */
    @Override
    public Document getData(String someData) throws IOException {
        if (getFormat(someData).equals(Format.URL)) {
            return getDataFromURL(someData);
        }
        return getDataFromFile(someData);
    }

    /**
     * Method returns document jsoup from some Url
     *
     * @param someURL
     * @return - document
     * @throws IOException
     */
    private Document getDataFromURL(String someURL) throws IOException {
        try {
            return Jsoup.connect(someURL).get();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new IOException();
        }
    }

    /**
     * Method returns document jsoup from some file
     *
     * @param path - path
     * @return - document
     * @throws IOException
     */
    private Document getDataFromFile(String path) throws IOException {
        String pathFile = Paths.get(path)
                .toAbsolutePath()
                .toString();
        try {
            return Jsoup.parse(new File(pathFile), "windows-1251");
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new IOException();
        }
    }

    /**
     * Method getting extension from given string and return object format
     *
     * @param any - string
     * @return - Format
     */
    private Format getFormat(String any) {
        if (any.matches("www.+") || any.matches("http.+")) {
            return Format.URL;
        }
        return Format.File;
    }

    /**
     * Enum Formats
     */
    private enum Format {
        URL,
        File
    }
}
