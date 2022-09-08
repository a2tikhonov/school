package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.add(student);
        return ResponseEntity.ok(createdStudent);
    }
    @GetMapping
    public ResponseEntity<Collection<Student>> getStudents(@RequestParam(required = false) Integer min,
                                                           @RequestParam(required = false) Integer max) {
        if (min != null && max != null && min > 0 && max > 0) {
            return ResponseEntity.ok(studentService.getByAgeBetween(min, max));
        }
        return ResponseEntity.ok(studentService.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/age")
    public ResponseEntity<Collection<Student>> getStudent(@RequestParam int age) {
        Collection<Student> students = studentService.filter(age);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        if (studentService.getStudentFaculty(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentService.getStudentFaculty(id));
    }

    @GetMapping("/get-amount")
    public ResponseEntity<Long> getAmountOfStudents() {
        return ResponseEntity.ok(studentService.getAmountOfStudents());
    }

    @GetMapping("/get-avg-age")
    public ResponseEntity<Float> getAvgOfStudentsAge() {
        return ResponseEntity.ok(studentService.getAvgOfStudentsAge());
    }

    @GetMapping("/get-5-last")
    public ResponseEntity<Collection<Student>> get5Last() {
        return ResponseEntity.ok(studentService.get5Last());
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.set(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.remove(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 400 * 400) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        studentService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/avatars")
    public ResponseEntity<Collection<Avatar>> getAvatars(@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize) {
        return ResponseEntity.ok(studentService.getAvatars(pageNumber, pageSize));
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream inputStream = Files.newInputStream(path);
             OutputStream outputStream = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            inputStream.transferTo(outputStream);
        }
    }


    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatar(id);
        if (avatar == null) {
            return ResponseEntity.badRequest().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

}
