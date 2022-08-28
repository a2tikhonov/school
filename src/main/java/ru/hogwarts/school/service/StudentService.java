package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student add(Student student) {
        return studentRepository.save(student);
    }

    public Collection<Student> get() {
        return studentRepository.findAll();
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(Long id) {
        return studentRepository.findById(id).get().getFaculty();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }


    public Student set(Student student) {
        return studentRepository.save(student);
    }

    public void remove(Long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> filter(int age) {
        return studentRepository.findByAge(age);
    }
}
