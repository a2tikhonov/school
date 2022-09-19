package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;

import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {

    InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/getPort")
    public ResponseEntity<Integer> getPort() {
        return ResponseEntity.ok(infoService.getPort());
    }

    @GetMapping("/integer")
    public ResponseEntity<Integer> getInteger() {
        return ResponseEntity.ok(infoService.getSum());
    }
}
