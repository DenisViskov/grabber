package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

/**
 * Class is an example show how to works quartz scheduler
 *
 * @author Денис Висков
 * @version 1.0
 * @since 13.05.2020
 */
public class AlertRabbit {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AlertRabbit.class);

    public static void main(String[] args) throws IOException {
        AlertRabbit alertRabbit = new AlertRabbit();
        Properties properties = alertRabbit.getResultProperties();
        try (Connection connection = alertRabbit.getDbConnection(properties)) {
            Scheduler scheduler = alertRabbit.schedulerBuilder(properties, connection);
            scheduler.start();
            Thread.sleep(10000);
            scheduler.shutdown(true);
        } catch (Exception se) {
            LOG.error(se.getMessage(), se);
        }
    }

    /**
     * Method has a building configurations for scheduler
     *
     * @param properties - properties
     * @param connection - connection for DB
     * @return - scheduler
     * @throws SchedulerException
     */
    private Scheduler schedulerBuilder(Properties properties, Connection connection) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDataMap data = new JobDataMap(Map.of("connection", connection));
        JobDetail job = newJob(Rabbit.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.valueOf(properties.getProperty("rabbit.interval")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
        return scheduler;
    }

    /**
     * Method returns ready properties
     *
     * @return - properties
     * @throws IOException
     */
    private Properties getResultProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new IOException();
        }
        return properties;
    }

    /**
     * Method returns connection with configuration for postgreSql
     *
     * @param properties - properties
     * @return - connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection getDbConnection(Properties properties) throws ClassNotFoundException, SQLException {
        Class.forName(properties.getProperty("driver-class-name"));
        return DriverManager.getConnection(properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password"));
    }

    /**
     * Inner static class for example show how to works scheduler
     */
    public static class Rabbit implements Job {

        /**
         * Method execute our planner work
         *
         * @param context - context
         * @throws JobExecutionException
         */
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try (PreparedStatement statement = connection.prepareStatement("insert into rabbit(created) values (?)")) {
                statement.setDate(1, new Date(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (SQLException throwables) {
                LOG.error(throwables.getMessage(), throwables);
            }
        }
    }
}

