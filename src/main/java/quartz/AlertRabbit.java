package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    public static void main(String[] args) throws IOException {
        AlertRabbit alertRabbit = new AlertRabbit();
        Properties properties = alertRabbit.getResultProperties();
        try (Connection connection = alertRabbit.getDbConnection(properties)) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
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
        } catch (SchedulerException | ClassNotFoundException | SQLException se) {
            se.printStackTrace();
        }
    }

    private Properties getResultProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        return properties;
    }

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
            System.out.println("Rabbit runs here ...");
        }
    }
}

