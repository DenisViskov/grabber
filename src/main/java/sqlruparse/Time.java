package sqlruparse;

/**
 * Interface for time
 *
 * @author Денис Висков
 * @version 1.0
 * @since 19.05.2020
 */
public interface Time<Time> {

    /**
     * Method should return T - time
     *
     * @param line - line
     * @return - T
     */
    Time ifLineBeginWithNumber(String line);

    /**
     * Method should return T - time
     *
     * @param line - line
     * @return - T
     */
    Time ifLineBeginWithWord(String line);
}
