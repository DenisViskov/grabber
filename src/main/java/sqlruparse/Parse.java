package sqlruparse;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for parsing
 *
 * @author Денис Висков
 * @version 1.0
 * @since 22.05.2020
 */
public interface Parse {

    /**
     * Method should parsing posts from given url
     *
     * @param link - url
     * @return - List of post
     */
    List<Post> list(String link) throws IOException;

    /**
     * Method should return Post with whole data
     *
     * @param link - url
     * @return - Post
     */
    Post detail(String link) throws IOException;

    /**
     * Method should filtering given posts
     *
     * @param filter - filter
     * @param list   - list of posts
     * @return - List of filtered posts
     */
    List<Post> filter(Predicate<Post> filter, List<Post> list);
}
