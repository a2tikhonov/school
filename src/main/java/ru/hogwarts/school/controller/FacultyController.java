package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.add(faculty);
        return ResponseEntity.ok(createdFaculty);
    }

    @GetMapping("{id}")
    public ResponseEntity getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.getById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculty);
        }
    }

    @GetMapping("{color}")
    public ResponseEntity getFaculty(@PathVariable String color) {
        Collection<Faculty> faculties = facultyService.filter(color);
        if (faculties == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculties);
        }
    }

    @PutMapping
    public ResponseEntity updateFaculty(@RequestBody Faculty faculty) {
        Faculty updatedStudent = facultyService.set(faculty.getId(), faculty);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(updatedStudent);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteFaculty(@RequestBody Faculty faculty) {
        Faculty deletedFaculty = facultyService.remove(faculty);
        if (deletedFaculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(deletedFaculty);
        }
    }
}
