package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FacultyService {

    private final Logger logger1 = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }


    public Faculty add(Faculty faculty) {
        logger1.debug("adding faculty by entity");
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> get() {
        logger1.debug("getting faculties collection");
        return facultyRepository.findAll();
    }

    public String findLogestFacultyName() {
        return facultyRepository.findAll().stream().map(p -> p.getName())
                .max(Comparator.comparing(String::valueOf)).orElse(null);

    }

    public Collection<Student> getAllStudentsInFaculty(Long id) {
        logger1.debug("getting all students in faculty id: {}", id);
        return studentRepository.findByFacultyId(id);
    }

    public Collection<Faculty> getByColorOrName(String color, String name) {
        logger1.debug("getting faculty by color {} and name {}", color, name);
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public Faculty getById(Long id) {
        logger1.debug("getting faculty by id: {}", id);
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty set(Faculty faculty) {
        logger1.debug("saving faculty by entity {}", faculty);
        return facultyRepository.save(faculty);
    }

    public void remove(Long id) {
        logger1.debug("removing faculty by id: {}", id);
        facultyRepository.deleteById(id);
    }


}
