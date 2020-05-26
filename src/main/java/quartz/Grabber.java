package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sqlruparse.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Class has main logic of app
 *
 * @author Денис Висков
 * @version 1.0
 * @since 26.05.2020
 */
public class Grabber implements Grab {

    /**
     * Config properties
     */
    private final Properties cfg = new Properties();

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(Grabber.class);

    /**
     * Method returns object store
     *
     * @param store - storage
     * @return - Store
     */
    public Store storeIn(Store store) {
        return store;
    }

    /**
     * Method execute initialize configuration properties from given path
     *
     * @param path - path
     * @throws IOException
     */
    public void cfg(Path path) throws IOException {
        try (InputStream in = new FileInputStream(new File(path
                .toAbsolutePath()
                .toString()))) {
            cfg.load(in);
        }
    }

    /**
     * Method returns running scheduler
     *
     * @return - Scheduler
     * @throws SchedulerException
     */
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * Method execute initialize Scheduler
     *
     * @param parse     - parse
     * @param store     - storage
     * @param scheduler -  scheduler
     * @throws SchedulerException
     */
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("rabbit.interval")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * Inner class which implements Job interface for works with quartz
     */
    public static class GrabJob implements Job {

        /**
         * Override method execute for quartz scheduler
         *
         * @param context - context
         * @throws JobExecutionException
         */
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            try {
                parse.filter(post -> post.getName().contains("java")
                                || post.getName().contains("Java"),
                        parse.list("https://www.sql.ru/forum/job-offers"))
                        .forEach(post -> store.save(post));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public Properties getCfg() {
        return cfg;
    }

    public static void main(String[] args) throws Exception {
        ProxyChanger.useThroughProxy();
        Grabber grab = new Grabber();
        grab.cfg(Paths.get("./src/main/resources/rabbit.properties"));
        Scheduler scheduler = grab.scheduler();
        Store store = grab.storeIn(new PsqlStore(grab.getCfg()));
        grab.init(new SqlRuMainPageParser(new StringConverter()), store, scheduler);
    }
}

