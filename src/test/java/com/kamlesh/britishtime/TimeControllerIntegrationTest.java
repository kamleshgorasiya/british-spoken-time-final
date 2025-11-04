package com.kamlesh.britishtime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TimeControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void restEndpointShouldReturnSpokenTime() {
        String url = "http://localhost:" + port + "/api/time/spoken?time=07:35";
        String body = this.restTemplate.getForObject(url, String.class);
        assertEquals("{\"input\":\"07:35\",\"spoken\":\"twenty five to eight\"}", body);
    }
}
