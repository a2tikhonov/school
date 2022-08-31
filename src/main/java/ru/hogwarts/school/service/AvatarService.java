package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentService studentService;

    private final AvatarRepository avatarRepository;

    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.getById(studentId);
        Path filePath = Path.of(avatarsDir, studentId + "." + getFileExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream inputStream = file.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024);) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar(filePath.toString(),
                file.getSize(), file.getContentType(), compressImage(filePath)));
        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long id) {
        return avatarRepository.findByStudentId(id).orElse(null);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf(".") + 1);
    }

    private byte[] compressImage(Path filePath) throws IOException {
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
}
