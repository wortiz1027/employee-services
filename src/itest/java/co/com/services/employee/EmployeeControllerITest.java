package co.com.services.employee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("itest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("TEST :: Bateria de pruebas de integracion para la informacion de los empleados registrados")
public class EmployeeControllerITest extends TestContanersConfig {

    @Autowired
    private MockMvc client;

    @Test
    @DisplayName("TEST :: Valida consulta de todos los empleados :: paginando los resultados")
    void itShouldReturnEmployeesInformation() throws Exception {
        String token = keycloakService.tokenManager().grantToken().getToken();

        this.client.perform( MockMvcRequestBuilders
                        .get("/employees/api/v1/?page=0&size=5")
                        .header("authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].code").isNumber());
    }

    @Test
    @DisplayName("TEST :: Valida consulta de un empleado por 'code'")
    void itShouldReturnEmployeesInformationDetailById() throws Exception {
        String token = keycloakService.tokenManager().grantToken().getToken();

        this.client.perform( MockMvcRequestBuilders
                        .get("/employees/api/v1/10001")
                        .header("authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").isNumber());
    }

    @Test
    @DisplayName("TEST :: Valida 'BAD_REQUEST' al consultar informacion del empleado")
    void itShouldReturnEmployeesInformationDetailCodeValueException() throws Exception {
        String token = keycloakService.tokenManager().grantToken().getToken();

        this.client.perform( MockMvcRequestBuilders
                        .get("/employees/api/v1/A")
                        .header("authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.BAD_REQUEST.name()));
    }

    @Test
    @DisplayName("TEST :: Valida 'BAD_REQUEST' al consultar informacion del empleado")
    void itShouldReturnEmployeesInformationDetailByIdNotFound() throws Exception {
        String token = keycloakService.tokenManager().grantToken().getToken();

        this.client.perform( MockMvcRequestBuilders
                        .get("/employees/api/v1/1234567")
                        .header("authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.NOT_FOUND.name()));
    }

}