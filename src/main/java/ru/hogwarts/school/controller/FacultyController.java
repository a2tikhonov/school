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

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.add(faculty);
        return ResponseEntity.ok(createdFaculty);
    }

    @GetMapping
    public ResponseEntity get(@RequestParam(required = false) String color, @RequestParam(required = false) String name) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.getByColor(color));
        }
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(facultyService.getByName(name));
        }
        return ResponseEntity.ok(facultyService.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.getById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculty);
        }
    }

    @GetMapping("/students")
    public ResponseEntity getAllStudentsInFaculty(@RequestParam Long id) {
        return ResponseEntity.ok(facultyService.getAllStudentsInFaculty(id));
    }

    @PutMapping
    public ResponseEntity updateFaculty(@RequestBody Faculty faculty) {
        Faculty updatedStudent = facultyService.set(faculty);
            return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFaculty(@RequestParam Long id) {
        facultyService.remove(id);
        return ResponseEntity.ok().build();
    }
}
