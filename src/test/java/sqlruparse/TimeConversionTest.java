package sqlruparse;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class TimeConversionTest {

    @Test
    public void toDateChangerTest() {
        List<LocalDateTime> expected = List.of(LocalDateTime.of(20,
                05,
                20,
                22,
                30),
                LocalDateTime.of(19,
                        3,
                        23,
                        22,
                        56));
        assertThat(expected, is(new TimeConversion().toDateChanger(List.of("20 май 20, 22:30",
                "23 мар 19, 22:56"))));
    }

    @Test
    public void whenWeHaveTodayTest() {
        List<LocalDateTime> expected = List.of(LocalDateTime.of(LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                22,
                30),
                LocalDateTime.of(LocalDateTime.now().getYear(),
                        LocalDateTime.now().getMonth(),
                        LocalDateTime.now().getDayOfMonth() - 1,
                        22,
                        56));
        assertThat(expected, is(new TimeConversion().toDateChanger(List.of("сегодня, 22:30",
                "вчера, 22:56"))));
    }
}