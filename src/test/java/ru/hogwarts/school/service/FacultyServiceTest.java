package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepositoryMock;

    @InjectMocks
    private FacultyService out;

    private final Faculty expectedFaculty = new Faculty(0L, "test", "test");

    @Test
    void add() {
        when(facultyRepositoryMock.save(expectedFaculty)).thenReturn(expectedFaculty);
        assertEquals(expectedFaculty, out.add(expectedFaculty));
    }

    @Test
    void get() {
        List<Faculty> expectedList = List.of(new Faculty(0L, "test", "test"),
                new Faculty(0L, "test1", "test1"),
                new Faculty(0L, "test2", "test2"),
                new Faculty(0L, "test3", "test3"));
        when(facultyRepositoryMock.findAll()).thenReturn(expectedList);
        assertEquals(expectedList, out.get());
    }

    @Test
    void getById() {
        when(facultyRepositoryMock.findById(1L)).thenReturn(Optional.of(expectedFaculty));
        assertEquals(expectedFaculty, out.getById(1L));
    }

    @Test
    void set() {
        when(facultyRepositoryMock.save(expectedFaculty)).thenReturn(expectedFaculty);
        assertEquals(expectedFaculty, out.set(expectedFaculty));
    }
}