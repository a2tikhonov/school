package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final Map<Long, Faculty> faculties;

    private static Long id = 0L;

    public FacultyService() {
        this.faculties = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacultyService that = (FacultyService) o;
        return Objects.equals(faculties, that.faculties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faculties);
    }

    public Faculty add(Faculty faculty) {
        return faculties.put(++id, faculty);
    }

    public Collection<Faculty> get() {
        return faculties.entrySet().stream().map(p -> p.getValue()).collect(Collectors.toList());
    }

    public Faculty getById(Long id) {
        return faculties.get(id);
    }

    public Faculty set(Long id, Faculty faculty) {
        return faculties.replace(id, faculty);
    }

    public Faculty remove(Faculty faculty) {
        return faculties.remove(faculty);
    }

    public Collection<Faculty> filter(String color) {
        return faculties.entrySet().stream()
                .filter(p -> p.getValue().getColor().equals(color))
                .map(n -> n.getValue()).collect(Collectors.toList());
    }

}
