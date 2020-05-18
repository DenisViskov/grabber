package sqlruparse;

/**
 * Class for works with JSOUP when i located on my job office
 *
 * @author Денис Висков
 * @version 1.0
 * @since 18.05.2020
 */
public class ProxyChanger {

    /**
     * Method turns on exit to network through Proxy
     */
    public static void useThroughProxy() {
        System.setProperty("https.proxyHost", "192.168.111.102");
        System.setProperty("https.proxyPort", "3128");
    }
}
