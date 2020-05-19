package sqlruparse;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

public class ProxyChangerTest {

    @Test
    public void useThroughProxyTest() {
        ProxyChanger.useThroughProxy();
        assertThat("192.168.111.102", is(System.getProperty("https.proxyHost")));
        assertThat("3128", is(System.getProperty("https.proxyPort")));
        assertThat(true, is(ProxyChanger.isStatus()));
    }

    @Test
    public void useThroughDefaultNetworkTest() {
        ProxyChanger.useThroughDefaultNetwork();
        assertThat(null, is(System.getProperty("https.proxyHost")));
        assertThat(null, is(System.getProperty("https.proxyPort")));
        assertThat(false, is(ProxyChanger.isStatus()));
    }
}