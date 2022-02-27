package co.com.services.employee;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

public class MySQLTest {

    @Test
    public void testSelect() {
        MySQLContainer mysql = new MySQLContainer("mysql:8.0.27");

        mysql.start();
        mysql.stop();

    }

}