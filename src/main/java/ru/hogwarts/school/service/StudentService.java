package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final Map<Long, Student> students;

    private static Long id = 0L;

    public StudentService() {
        this.students = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentService that = (StudentService) o;
        return Objects.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(students);
    }

    public Student add(Student student) {
        return students.put(++id, student);
    }

    public Collection<Student> get() {
        return students.entrySet().stream().map(p -> p.getValue()).collect(Collectors.toList());
    }

    public Student getById(Long id) {
        return students.get(id);
    }

    public Student set(Long id, Student student) {
        return students.replace(id, student);
    }

    public Student remove(Student student) {
        return students.remove(student);
    }

    public Collection<Student> filter(int age) {
        return students.entrySet().stream()
                .filter(p -> p.getValue().getAge() == age)
                .map(n -> n.getValue()).collect(Collectors.toList());
    }
}
