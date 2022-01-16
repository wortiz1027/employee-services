package co.com.services.employee.service.impl;

import co.com.services.employee.exceptions.FileSizeException;
import co.com.services.employee.exceptions.FileTypeException;
import co.com.services.employee.exceptions.UploadFileException;
import co.com.services.employee.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService implements IFileService {

    private static final String TEMPLATE_FILE_PATH = "%s/%s";

    @Value("${files.upload.directory}")
    private String DIRECTORY;

    @Override
    public void savefile(MultipartFile mpf) {
        try {
            if (!isValidFile(mpf.getOriginalFilename()))
                throw new FileTypeException("Invalid File extension");

            if (mpf.getSize() > 5000000)
                throw new FileSizeException("File size is too long");

            var filename = String.format(this.TEMPLATE_FILE_PATH, this.DIRECTORY, String.format("%s-%s", UUID.randomUUID(), mpf.getOriginalFilename()));

            File file = new File(filename);

            if (!file.exists()) file.createNewFile();

            try ( FileOutputStream fos = new FileOutputStream(file) ) {
                fos.write(mpf.getBytes());
            }
        } catch (IOException ioe) {
            throw new UploadFileException("There is an error uploading file");
        }
    }

    private boolean isValidFile(String name) {
        return "pdf".equals(name.substring(name.lastIndexOf(".") + 1));
    }

}