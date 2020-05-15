package quartz;

import org.junit.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class AlertRabbitTest {

    private static Connection connection;
    private static Properties properties;

    @BeforeClass
    public static void setUp() throws Exception {
        try (InputStream inputStream = AlertRabbitTest.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties = new Properties();
            properties.load(inputStream);
            Class.forName(properties.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("username"),
                    properties.getProperty("password"));
        }
    }

    @Before
    public void ifNotEmptyBase() {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select count(*) from rabbit");
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                statement.executeUpdate("delete from rabbit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mainTest() throws IOException {
        AlertRabbit.main(new String[]{});
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select count(*) from rabbit");
            resultSet.next();
            assertThat(resultSet.getInt(1), is(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        connection.close();
    }
}