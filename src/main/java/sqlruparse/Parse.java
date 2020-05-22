package sqlruparse;

import java.util.List;

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
    List<Post> list(String link);

    /**
     * Method should return Post with whole data
     *
     * @param link - url
     * @return - Post
     */
    Post detail(String link);
}
