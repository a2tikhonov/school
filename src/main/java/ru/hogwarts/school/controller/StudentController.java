package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity createStudent(@RequestBody Student student) {
        Student createdStudent = studentService.add(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("{id}")
    public ResponseEntity getStudent(@PathVariable Long id) {
        Student student = studentService.getById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(student);
        }
    }

    @GetMapping("{age}")
    public ResponseEntity getStudent(@PathVariable int age) {
        Collection<Student> students = studentService.filter(age);
        if (students == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(students);
        }
    }

    @PutMapping
    public ResponseEntity updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.set(student.getId(), student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedStudent);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteStudent(@RequestBody Student student) {
        Student deletedStudent = studentService.remove(student);
        if (deletedStudent == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(deletedStudent);
        }
    }

}
