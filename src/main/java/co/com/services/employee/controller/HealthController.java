package co.com.services.employee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping("ping")
    public ResponseEntity<String> all() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

}