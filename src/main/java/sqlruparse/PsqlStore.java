package sqlruparse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Class has realizes storage for Post
 *
 * @author Денис Висков
 * @version 1.0
 * @since 25.05.2020
 */
public class PsqlStore implements Store, AutoCloseable {

    /**
     * Connection
     */
    private Connection cnn;

    /**
     * Logger
     */
    private final static Logger LOG = LoggerFactory.getLogger(PsqlStore.class);

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password"));
        } catch (SQLException | ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public PsqlStore(Connection cnn) {
        this.cnn = cnn;
    }

    /**
     * Method saves post to storage
     *
     * @param post - post
     * @return - true or false in dependency of result
     */
    @Override
    public boolean save(Post post) {
        boolean result = false;
        try (PreparedStatement statement = cnn.prepareStatement("insert into post(name,text,link,created) values(?,?,?,?)")) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getUrl());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            result = statement.executeUpdate() > 0 ? true : false;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method returns all posts from storage
     *
     * @return - list of posts
     */
    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        try (Statement statement = cnn.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from post");
            while (resultSet.next()) {
                result.add(new Post(resultSet.getString("link"),
                        resultSet.getString("name"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("created").toLocalDateTime()));

            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Method looking for post by given id
     *
     * @param id - id
     * @return - Post
     */
    public Post findById(String id) {
        Post result = null;
        try (PreparedStatement statement = cnn.prepareStatement("select * from post where id=?")) {
            statement.setInt(1, Integer.valueOf(id));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result = new Post(resultSet.getString("link"),
                        resultSet.getString("name"),
                        resultSet.getString("text"),
                        resultSet.getTimestamp("created").toLocalDateTime());
            }
            if (result == null) {
                throw new NoSuchElementException("Post with given ID not found");
            }
        } catch (NoSuchElementException | SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Override method from AutoClosable interface
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}
