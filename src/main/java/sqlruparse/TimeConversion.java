package sqlruparse;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class is a time conversion
 *
 * @author Денис Висков
 * @version 1.0
 * @since 18.05.2020
 */
public class TimeConversion {

    /**
     * Default formatter
     */
    private final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd.MM.yy' at 'HH:mm");

    /**
     * Month
     */
    private final Map<String, Integer> month = new HashMap<>();

    public TimeConversion() {
        month.put("янв", 1);
        month.put("фев", 2);
        month.put("мар", 3);
        month.put("апр", 4);
        month.put("май", 5);
        month.put("июн", 6);
        month.put("июл", 7);
        month.put("авг", 8);
        month.put("сен", 9);
        month.put("окт", 10);
        month.put("ноя", 11);
        month.put("дек", 12);
    }

    /**
     * Method returns converted list from string to LocalDateTime list
     *
     * @param dates - dates
     * @return - List of LocalDateTime
     */
    public List<LocalDateTime> toDateChanger(List<String> dates) {
        return dates.stream()
                .map(line -> line.matches("^\\d+.+") ? ifLineBeginWithNumber(line)
                        : ifLineBeginWithWord(line)
                ).collect(Collectors.toList());
    }

    /**
     * Method is converting line to LocalDateTime
     *
     * @param line - line
     * @return - date LocalDateTime
     */
    private LocalDateTime ifLineBeginWithNumber(String line) {
        String[] split = line.split(" ");
        int year = Integer.parseInt(split[2].replaceFirst(",", ""));
        int day = Integer.parseInt(split[0]);
        Month month = Month.of(this.month.get(split[1]));
        int hour = Integer.valueOf(split[3].split(":")[0]);
        int minute = Integer.valueOf(split[3].split(":")[1]);
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute);
        return time;
    }

    /**
     * Method is converting line to LocalDateTime
     *
     * @param line - line
     * @return - date LocalDateTime
     */
    private LocalDateTime ifLineBeginWithWord(String line) {
        String[] split = line.split(" ");
        int year = LocalDateTime.now().getYear();
        int day = split[0].contains("сегодня") ? LocalDateTime.now().getDayOfMonth()
                : LocalDateTime.now().getDayOfMonth() - 1;
        Month month = LocalDateTime.now().getMonth();
        int hour = Integer.valueOf(split[1].split(":")[0]);
        int minute = Integer.valueOf(split[1].split(":")[1]);
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute);
        return time;
    }

    public DateTimeFormatter getDefaultFormatter() {
        return defaultFormatter;
    }
}
