package co.com.services.employee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;


@ActiveProfiles("itest")
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient(timeout = "36000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
//@Sql({ "file:src/itest/resources/database/employees.sql" })
public class EmployeeControllerITest {

    @Autowired
    private WebTestClient client;

    @Test
    void itShouldReturnPositionAndMessage() throws Exception {
        this.client
                // Create a GET request to test an endpoint
                .get().uri("/employees/api/v1")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                // and use the dedicated DSL to test assertions against the response
                .expectStatus().isOk();
    }

}