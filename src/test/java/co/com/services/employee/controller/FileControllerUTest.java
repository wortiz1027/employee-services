package co.com.services.employee.controller;

import co.com.services.employee.service.IFileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bateria de pruebas para FileController")
class FileControllerUTest {

    @Mock
    IFileService service;

    @InjectMocks
    FileController underTest;

    @Test
    @DisplayName("Test :: Upload file")
    void itShouldReturnFileUploaded() {
        doNothing().when(this.service).savefile(any(MultipartFile.class));

        var result = this.underTest.upload(file());

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo("UPLOADED");
    }

    private MockMultipartFile file() {
        MockMultipartFile file = new MockMultipartFile("file",
                                            "hello.pdf",
                                                        MediaType.TEXT_PLAIN_VALUE,
                                                        "Hello, World!".getBytes());

        return file;
    }

}