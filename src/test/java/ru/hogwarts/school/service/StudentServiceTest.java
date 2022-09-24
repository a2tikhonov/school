package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepositoryMock;

    @InjectMocks
    private StudentService out;

    private final Student expectedStudent = new Student(0L, "test", 18);

    @Test
    void add() {
        when(studentRepositoryMock.save(expectedStudent)).thenReturn(expectedStudent);
        assertEquals(expectedStudent, out.add(expectedStudent));
    }

    @Test
    void get() {
        List<Student> expectedList = List.of(new Student(0L, "test", 18),
                new Student(0L, "test1", 20),
                new Student(0L, "test2", 19),
                new Student(0L, "test3", 22));
        when(studentRepositoryMock.findAll()).thenReturn(expectedList);
        assertEquals(expectedList, out.get());
    }

    @Test
    void getById() {
        when(studentRepositoryMock.findById(1L)).thenReturn(Optional.of(expectedStudent));
        assertEquals(expectedStudent, out.getById(1L));
    }

    @Test
    void set() {
        when(studentRepositoryMock.save(expectedStudent)).thenReturn(expectedStudent);
        assertEquals(expectedStudent, out.set(expectedStudent));
    }

}