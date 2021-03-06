package sqlruparse;

import java.io.IOException;

/**
 * Interface DataConverter for convert from difference data to our data
 *
 * @author Денис Висков
 * @version 1.0
 * @since 19.05.2020
 */
public interface DataConverter<Value, Data> {

    /**
     * Method should return value from some Data
     *
     * @param someData - someData
     * @return - Value
     */
    Value getData(Data someData) throws IOException;
}
