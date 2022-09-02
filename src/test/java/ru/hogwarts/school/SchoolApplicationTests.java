package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void createStudent() {
        Student student = new Student(1L, "Alex", 18);
        Faculty faculty = new Faculty(1L, "faculty1", "red");
        Assertions.assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class)).isEqualTo(faculty);
        student.setFaculty(faculty);
        Assertions.assertThat(this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class)).isEqualTo(student);
        this.testRestTemplate.delete("http://localhost:" + port + "/student/1");
        this.testRestTemplate.delete("http://localhost:" + port + "/faculty/1");
    }

    @Test
    void getStudents() {
        Student student = new Student(null, "Alex", 18);
        Faculty faculty = new Faculty(1L, "faculty1", "red");
        student.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        Student student1 = new Student(null, "Oleg", 20);
        student1.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student1, Student.class);
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student", String.class ))
                .contains(Arrays.asList("Alex", "Oleg", "faculty1", "red"));
    }

    @Test
    void getStudent() {
        Student student = new Student(null, "Alex", 18);
        Faculty faculty = new Faculty(1L, "faculty1", "red");
        student.setFaculty(faculty);
        Student student1 = new Student(2L, "Oleg", 23);
        student1.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student1, Student.class);
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student/2", Student.class ))
                .isEqualTo(student1);
    }

    @Test
    void getStudentsByAge() {
        Student student = new Student(null, "Alex", 18);
        Faculty faculty = new Faculty(1L, "faculty1", "red");
        student.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        Student student1 = new Student(2L, "Oleg", 20);
        student1.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student1, Student.class);
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student/age?age=20", String.class))
                .contains(Arrays.asList("Oleg"));
    }

    @Test
    void getStudentFaculty() {
        Student student = new Student(null, "Alex", 18);
        Faculty faculty = new Faculty(1L, "faculty1", "red");
        Faculty faculty1 = new Faculty(2L, "faculty2", "green");
        student.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty1, Faculty.class);
        Student student1 = new Student(2L, "Oleg", 20);
        student1.setFaculty(faculty1);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student1, Student.class);
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student/2/faculty", String.class))
                .contains(Arrays.asList("faculty2", "green"));
    }

    @Test
    void updateStudent() {
        Student student = new Student(1L, "Alex", 18);
        Faculty faculty = new Faculty(1L, "faculty1", "red");
        student.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        student.setName("Oleg");
        this.testRestTemplate.put("http://localhost:" + port + "/student", student, Student.class);
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student/1", String.class))
                .contains(Arrays.asList("Oleg"));
    }

    @Test
    void deleteStudent() {
        Student student = new Student(1L, "Alex", 18);
        Faculty faculty = new Faculty(1L, "faculty1", "red");
        student.setFaculty(faculty);
        Student student1 = new Student(2L, "Oleg", 20);
        student1.setFaculty(faculty);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student, Student.class);
        this.testRestTemplate.postForObject("http://localhost:" + port + "/student", student1, Student.class);
        this.testRestTemplate.delete("http://localhost:" + port + "/student/1", Student.class);
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/student/1", Student.class))
                .isNull();
    }

}
