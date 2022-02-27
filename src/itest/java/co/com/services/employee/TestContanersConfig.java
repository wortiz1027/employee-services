package co.com.services.employee;

import org.keycloak.admin.client.Keycloak;

import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Testcontainers
public abstract class TestContanersConfig {

    private static final String COMPANY_SERVICE_REALM_NAME = "realm-services";
    private static final String EMPLOYEE_SERVICE_CLIENT_ID = "employee-service";
    private static final String EMPLOYEE_SERVICE_CLIENT_SECRET = "4ZUSstEL0kqe1VJaF8qw8ny0BsDSAa8w";
    private static final String USER_USERNAME = "wortiz";
    private static final String USER_PASSWORD = "junior2022++";
    private static final List<String> EMPLOYEE_SERVICE_ROLES = Collections.singletonList("USER");

    private static final Integer KEYCLOAK_SERVER_TIMEOUT  = 2;
    private static final Integer KEYCLOAK_SERVER_PORT     = 8080;
    private static final String  KEYCLOAK_DB_VENDOR       = "h2";
    private static final String  KEYCLOAK_ADMIN_REALM     = "master";
    private static final String  KEYCLOAK_ADMIN_CLIENT_ID = "admin-cli";
    private static final String  KEYCLOAK_ADMIN_USERNAME  = "admin";
    private static final String  KEYCLOAK_ADMIN_PASSWORD  = "admin2022++";
    private static final String  KEYCLOAK_AUTH_PATH       = "/auth";
    private static final String  KEYCLOAK_URL_TEMPLATE    = "http://%s:%s" + KEYCLOAK_AUTH_PATH;
    private static final String  KEYCLOAK_PRINCIPAL_ATTIB = "preferred_username";
    private static final String  KEYCLOAK_SSL_ENABLED     = "external";

    private static final GenericContainer<?> KEYCLOAK_SERVER = new GenericContainer<>("jboss/keycloak:16.1.1");

    protected static Keycloak keycloakService;

    //@Container
    static final MySQLContainer<?> MySQL;

    static {
        MySQL = new MySQLContainer<>("mysql:8.0.27")
                                        .withUsername("wortiz")
                                        .withPassword("server2022++")
                                        .withDatabaseName("employees")
                                        .withInitScript("database/employees.sql")
                                        .withCommand("mysqld --default-authentication-plugin=mysql_native_password")
                                        .withReuse(true);
        MySQL.start();
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MySQL::getJdbcUrl);
        registry.add("spring.datasource.username", MySQL::getUsername);
        registry.add("spring.datasource.password", MySQL::getPassword);
    }

    @DynamicPropertySource
    private static void properties(DynamicPropertyRegistry registry) {
        Map<String, String> config = new HashMap<>();
        config.put("KEYCLOAK_USER", KEYCLOAK_ADMIN_USERNAME);
        config.put("KEYCLOAK_PASSWORD", KEYCLOAK_ADMIN_PASSWORD);
        config.put("DB_VENDOR", KEYCLOAK_DB_VENDOR);

        KEYCLOAK_SERVER.withExposedPorts(KEYCLOAK_SERVER_PORT)
                          .withEnv(config)
                          .waitingFor(Wait.forHttp(KEYCLOAK_AUTH_PATH)
                                          .forPort(KEYCLOAK_SERVER_PORT)
                                          .withStartupTimeout(Duration.ofMinutes(KEYCLOAK_SERVER_TIMEOUT)))
                        .start();

        var serverUrl = String.format(KEYCLOAK_URL_TEMPLATE, KEYCLOAK_SERVER.getHost(), KEYCLOAK_SERVER.getMappedPort(KEYCLOAK_SERVER_PORT));
        registry.add("keycloak.auth-server-url", () -> serverUrl);
        registry.add("keycloak.realm", () -> COMPANY_SERVICE_REALM_NAME);
        registry.add("keycloak.resource", () -> EMPLOYEE_SERVICE_CLIENT_ID);
        registry.add("keycloak.credentials.secret", () -> EMPLOYEE_SERVICE_CLIENT_SECRET);
        registry.add("keycloak.ssl-required", () -> KEYCLOAK_SSL_ENABLED);
        registry.add("keycloak.bearer-only", () -> Boolean.TRUE);
        registry.add("keycloak.use-resource-role-mappings", () -> Boolean.TRUE);
        registry.add("keycloak.principal-attribute", () -> KEYCLOAK_PRINCIPAL_ATTIB);

        if (keycloakService == null)
            setUpKeycloak(serverUrl);

    }

    private static void setUpKeycloak(String serverUrl) {
        Keycloak administrator = KeycloakBuilder.builder()
                                                    .serverUrl(serverUrl)
                                                    .realm(KEYCLOAK_ADMIN_REALM)
                                                    .username(KEYCLOAK_ADMIN_USERNAME)
                                                    .password(KEYCLOAK_ADMIN_PASSWORD)
                                                    .clientId(KEYCLOAK_ADMIN_CLIENT_ID)
                                                .build();

        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(COMPANY_SERVICE_REALM_NAME);
        realm.setEnabled(Boolean.TRUE);

        ClientRepresentation client = new ClientRepresentation();
        client.setId(EMPLOYEE_SERVICE_CLIENT_ID);
        client.setSecret(EMPLOYEE_SERVICE_CLIENT_SECRET);
        client.setDirectAccessGrantsEnabled(Boolean.TRUE);

        realm.setClients(Collections.singletonList(client));

        Map<String, List<String>> roles = new HashMap<>();
        roles.put(EMPLOYEE_SERVICE_CLIENT_ID, EMPLOYEE_SERVICE_ROLES);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(USER_PASSWORD);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(USER_USERNAME);
        user.setEnabled(Boolean.TRUE);
        user.setCredentials(Collections.singletonList(credential));
        user.setClientRoles(roles);

        realm.setUsers(Collections.singletonList(user));

        administrator.realms().create(realm);

        keycloakService = KeycloakBuilder.builder()
                                            .serverUrl(serverUrl)
                                            .realm(COMPANY_SERVICE_REALM_NAME)
                                            .username(USER_USERNAME)
                                            .password(USER_PASSWORD)
                                            .clientId(EMPLOYEE_SERVICE_CLIENT_ID)
                                            .clientSecret(EMPLOYEE_SERVICE_CLIENT_SECRET)
                                        .build();
    }

}