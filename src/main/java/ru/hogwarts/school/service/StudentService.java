package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private Integer count = 0;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }


    public Student add(Student student) {
        logger.debug("adding student: {}", student);
        return studentRepository.save(student);
    }

    public Collection<Student> get() {
        logger.debug("getting students collection");
        return studentRepository.findAll();
    }

    public void getStudents() {
        ArrayList<Student> students = new ArrayList<>(get());
        System.out.println("students.get(0) = " + students.get(0).getName());
        System.out.println("students.get(1) = " + students.get(1).getName());
        new Thread(() -> {
            System.out.println("students.get(2) = " + students.get(2).getName());
            System.out.println("students.get(3) = " + students.get(3).getName());
        }).start();
        new Thread(() -> {
            System.out.println("students.get(4) = " + students.get(4).getName());
            System.out.println("students.get(5) = " + students.get(5).getName());
        }).start();
    }

    private synchronized void gets(ArrayList<Student> students) {
        System.out.println(this.count + " " + students.get(this.count++).getName());
        System.out.println(this.count + " " + students.get(this.count++).getName());
    }


    public void getStudentsSync() {
        count = 0;
        ArrayList<Student> students = new ArrayList<>(get());
        System.out.println(count + " " + students.get(count++).getName());
        System.out.println(count + " " + students.get(count++).getName());
        new Thread(() -> {
            gets(students);
        }).start();
        new Thread(() -> {
            gets(students);
        }).start();
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        logger.debug("getting students collection with age between: {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getStudentFaculty(Long id) {
        logger.debug("getting student faculty by student id: {}", id);
        return studentRepository.findById(id).map(Student::getFaculty).orElse(null);
    }

    public Student getById(Long id) {
        logger.debug("getting student by id: {}", id);
        return studentRepository.findById(id).orElse(null);
    }


    public Student set(Student student) {
        logger.debug("saving student by entity: {}", student);
        return studentRepository.save(student);
    }

    public void remove(Long id) {
        logger.debug("removing student by id: {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> filter(int age) {
        logger.debug("getting students collection by age: {}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<String> findByNameFirstLetter(Character letter) {
        return studentRepository.findAll().stream().map(n -> n.getName().toUpperCase()).filter(p -> p.startsWith(letter.toString()))
                .sorted().collect(Collectors.toList());
    }

    public Double getAverageAge() {
        return studentRepository.findAll().stream().mapToDouble(p -> p.getAge()).average().orElse(-1);
    }

    public void deleteAvatar(Long id) throws  IOException {
        logger.debug("deleting student avatar by id: {}", id);
        Avatar avatar = avatarRepository.findByStudentId(id).orElse(null);
        if (avatar != null) {
            File file = new File(avatar.getFilePath());
            file.delete();
        }
        avatarRepository.deleteById(id);

    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.debug("uploading student avatar by id: {}", studentId);
        Student student = getById(studentId);
        Path filePath = Path.of(avatarsDir, studentId + "." + getFileExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024))
        {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(compressImage(filePath));
        avatar.setStudent(student);
        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long id) {
        logger.debug("finding student avatar by id: {}", id);
        return avatarRepository.findByStudentId(id).orElse(null);
    }

    public Collection<Avatar> getAvatars(Integer pageNumber, Integer pageSize) {
        logger.debug("getting students avatars by pageNumber and Size: {} and {}", pageNumber, pageSize);
        PageRequest request = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(request).getContent();
    }

    private String getFileExtension(String fileName) {
        logger.debug("getting file extension: {}", fileName);
        return fileName.substring(fileName.indexOf(".") + 1);
    }

    private byte[] compressImage(Path filePath) throws IOException {
        logger.debug("compressing avatar image located in: {}", filePath);
        try (InputStream inputStream = Files.newInputStream(filePath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
            int height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, bufferedImage.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(preview, getFileExtension(filePath.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    public Long getAmountOfStudents() {
        logger.debug("finding amount of students");
        return studentRepository.findAmountOfStudents();
    }

    public Float getAvgOfStudentsAge() {
        logger.debug("getting average students age");
        return studentRepository.findAverageAgeOfStudents();
    }

    public Collection<Student> get5Last() {
        logger.debug("getting last five students from db");
        return studentRepository.findLast5Students();
    }

    public Collection<Student> getStudentsByName(String name) {
        logger.debug("getting students collection by name: {}", name);
        return studentRepository.getStudentsByName(name);
    }
}
