
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class Demo_java_data {

    private RestTemplate restTemplate = new RestTemplate();

    private String domainUrl="http://localhost:8084/";

    @Test
    public void ticker() throws Exception {
        System.out.println(restTemplate.getForObject(domainUrl+"/data/v2/ticker?currency=qtum_btc", String.class));
    }

    @Test
    public void depth() throws Exception {
        System.out.println(restTemplate.getForObject(domainUrl+"/data/v2/depth?currency=qtum_btc&size=20&merge=0.1", String.class));
    }

    @Test
    public void trades() throws Exception {
        System.out.println(restTemplate.getForObject(domainUrl+"/data/v2/trades?currency=qtum_btc", String.class));
    }

    @Test
    public void kline() throws Exception {
        System.out.println(restTemplate.getForObject(domainUrl+"/data/v2/kline?currency=qtum_btc&type=1&size=1", String.class));
    }


}