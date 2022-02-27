package co.com.services.employee.service;

import co.com.services.employee.exceptions.FileSizeException;
import co.com.services.employee.exceptions.FileTypeException;
import co.com.services.employee.exceptions.UploadFileException;
import co.com.services.employee.service.impl.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("Bateria de pruebas para FileService")
class FileServiceUTest {

    IFileService underTest;

    @BeforeEach
    public void init() {
        this.underTest = new FileService();
    }

    @Test
    @DisplayName("Test :: Validate exception file type")
    void itShouldValidateFileTypeException() {
        ReflectionTestUtils.setField(this.underTest, "DIRECTORY", "/tmp");
        assertThrows(FileTypeException.class, () ->this.underTest.savefile(fileInvalidType()));
    }

    @Test
    @DisplayName("Test :: Validate exception file size")
    void itShouldValidateFileSizeException() {
        ReflectionTestUtils.setField(this.underTest, "DIRECTORY", "/tmp");
        assertThrows(FileSizeException.class, () ->this.underTest.savefile(fileInvalidSize()));
    }

    @Test
    @DisplayName("Test :: Validate save file success")
    void itShouldCreateFile() {
        ReflectionTestUtils.setField(this.underTest, "DIRECTORY", "/tmp");
        this.underTest.savefile(file());
    }

    @Test
    @DisplayName("Test :: Validate IOException saving file")
    void itShouldLauchIOException() {
        ReflectionTestUtils.setField(this.underTest, "DIRECTORY", "c:///");
        assertThrows(UploadFileException.class, () -> this.underTest.savefile(fileIOException()));
    }

    private MockMultipartFile fileInvalidType() {
        MockMultipartFile file = new MockMultipartFile("file",
                "hello.jpeg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());

        return file;
    }

    private MockMultipartFile fileInvalidSize() {
        byte[] size = new byte[1024 * 1024 * 15];
        MockMultipartFile file = new MockMultipartFile("file",
                "hello.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                size);

        return file;
    }

    private MockMultipartFile file() {
        MockMultipartFile file = new MockMultipartFile("file",
                "hello.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());

        return file;
    }

    private MockMultipartFile fileIOException() {
        MockMultipartFile file = new MockMultipartFile("file",
                "hello.pdf",
                MediaType.TEXT_PLAIN_VALUE,
                "".getBytes());

        return file;
    }

}