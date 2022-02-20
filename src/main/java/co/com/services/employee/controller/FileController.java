package co.com.services.employee.controller;

import co.com.services.employee.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files/api/v1")
@RequiredArgsConstructor
public class FileController {

    private final IFileService service;

    @PostMapping(value ="/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        this.service.savefile(file);
        return new ResponseEntity<>("UPLOADED", HttpStatus.CREATED);
    }

}