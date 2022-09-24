package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.LongStream;

@Service
public class InfoService {

    @Value("${server.port}")
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public Long getSum() {
        return  LongStream.rangeClosed(1L, 1_000_000L).reduce(0L, Long::sum);
    }

}
