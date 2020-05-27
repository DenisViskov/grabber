package quartz;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.*;
import sqlruparse.Parse;
import sqlruparse.Post;
import sqlruparse.PsqlStore;
import sqlruparse.Store;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class GrabberTest {

    @Test
    public void storeInTest() {
        Grabber grabber = new Grabber();
        Store sql = new PsqlStore(mock(Connection.class));
        Store out = grabber.storeIn(sql);
        assertThat(out, is(sql));
    }

    @Test
    public void schedulerTest() throws SchedulerException {
        Grabber grabber = new Grabber();
        Scheduler scheduler = grabber.scheduler();
        assertNotNull(scheduler);
    }

    @Test
    public void initTest() throws SchedulerException, IOException {
        Grabber grabber = new Grabber();
        grabber.cfg();
        Parse parse = mock(Parse.class);
        Store store = mock(Store.class);
        Scheduler scheduler = mock(Scheduler.class);
        grabber.init(parse, store, scheduler);
        verify(scheduler).scheduleJob(any(), any());
    }

    @Test
    public void executeTest() throws IOException, JobExecutionException {
        Grabber.GrabJob job = new Grabber.GrabJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        JobDetail detail = mock(JobDetail.class);
        Parse parse = mock(Parse.class);
        Store store = mock(Store.class);
        JobDataMap map = new JobDataMap();
        map.put("parse", parse);
        map.put("store", store);
        when(context.getJobDetail()).thenReturn(detail);
        when(detail.getJobDataMap()).thenReturn(map);
        when(parse.filter(any(), any())).thenReturn(List.of(new Post("",
                "java",
                "",
                LocalDateTime.now())));
        job.execute(context);
        verify(parse).list(anyString());
        verify(store).save(any());
    }
}