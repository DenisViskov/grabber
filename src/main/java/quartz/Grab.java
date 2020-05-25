package quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import sqlruparse.Parse;
import sqlruparse.Store;

/**
 * Interface for start app
 *
 * @author Денис Висков
 * @version 1.0
 * @since 25.05.2020
 */
public interface Grab {
    /**
     * Method should do initialization app
     *
     * @param parse     - parse
     * @param store     - storage
     * @param scheduler -  scheduler
     * @throws SchedulerException
     */
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
