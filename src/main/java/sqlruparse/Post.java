package sqlruparse;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class is a Post data
 *
 * @author Денис Висков
 * @version 1.0
 * @since 20.05.2020
 */
public class Post {
    /**
     * URL
     */
    private final String url;

    /**
     * Name
     */
    private final String name;

    /**
     * Description
     */
    private final String description;

    /**
     * Created time
     */
    private final LocalDateTime created;

    public Post(String url, String name, String description, LocalDateTime created) {
        this.url = url;
        this.name = name;
        this.description = description;
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

        public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(url, post.url)
                && Objects.equals(name, post.name)
                && Objects.equals(description, post.description)
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name, description, created);
    }

    @Override
    public String toString() {
        return "Post:" + System.lineSeparator()
                + "url:" + " " + url + System.lineSeparator()
                + "name:" + " " + name + System.lineSeparator()
                + "description:" + " " + description + System.lineSeparator()
                + "created:" + " " + created.format(new TimeConversion()
                .getDefaultFormatter()) + System.lineSeparator();
    }
}
