package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import java.util.Collection;
import java.util.Collections;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty add(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> get() {
        return facultyRepository.findAll();
    }

    public Collection<Student> getAllStudentsInFaculty(Long id) {
        return facultyRepository.findAllById(id);
    }

    public Collection<Faculty> getByColorOrName(String color, String name) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public Faculty getById(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty set(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void remove(Long id) {
        facultyRepository.deleteById(id);
    }


}
