package sqlruparse;

import java.util.List;

/**
 * Interface for store data
 *
 * @author Денис Висков
 * @version 1.0
 * @since 25.05.2020
 */
public interface Store {
    /**
     * Method should save data to our storage
     *
     * @param post - post
     */
    boolean save(Post post);

    /**
     * Method should return all element from storage
     *
     * @return - list of posts
     */
    List<Post> getAll();
}
