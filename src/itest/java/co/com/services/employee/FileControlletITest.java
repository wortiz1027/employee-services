package co.com.services.employee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("itest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("TEST :: Bateria de pruebas de integracion para la gestion de archivos")
public class FileControlletITest extends TestContanersConfig {

    @Autowired
    private MockMvc client;

    @Test
    @DisplayName("TEST :: Valida la carga correcta de archivos")
    void itShouldUploadFiles() throws Exception {
        String token = keycloakService.tokenManager().grantToken().getToken();

        MockMultipartFile file = new MockMultipartFile("file",
                                                    "hello.pdf",
                                                       MediaType.TEXT_PLAIN_VALUE,
                                                       "Hello, World!".getBytes());

        this.client.perform(multipart("/files/api/v1/upload")
                                .file(file)
                                .header("authorization", String.format("Bearer %s", token)))
                   .andExpect(status().isCreated())
                   .andExpect(MockMvcResultMatchers.content().string("UPLOADED"));
    }

    @Test
    @DisplayName("TEST :: Valida la carga correcta de archivos")
    void itShouldValidateFileTypeException() throws Exception {
        String token = keycloakService.tokenManager().grantToken().getToken();

        MockMultipartFile file = new MockMultipartFile("file",
                "hello.jpeg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());

        this.client.perform(multipart("/files/api/v1/upload")
                                .file(file)
                                .header("authorization", String.format("Bearer %s", token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("BAD_REQUEST")));
    }

    @Test
    @DisplayName("TEST :: Valida el tamanio del archivo")
    void itShouldValidateFileSizeException() throws Exception {
        byte[] size = new byte[1024 * 1024 * 15];
        String token = keycloakService.tokenManager().grantToken().getToken();

        MockMultipartFile file = new MockMultipartFile("file",
                "hello.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                size);

        this.client.perform(multipart("/files/api/v1/upload")
                                .file(file)
                                .header("authorization", String.format("Bearer %s", token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("File size is too long")));
    }

}