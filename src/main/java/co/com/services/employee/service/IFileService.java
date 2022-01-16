package co.com.services.employee.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    void savefile(MultipartFile file);

}