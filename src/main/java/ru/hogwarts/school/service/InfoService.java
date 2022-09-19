package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class InfoService {

    @Value("${server.port}")
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public Integer getSum() {
        int sum = 0;
        for (int i = 1; i <= 1_000_000; i++) {
            sum += i;
        }
        return sum;
    }

/*    long startTime = System.currentTimeMillis();
    Integer suma = Stream.iterate(1, a -> a +1) .limit(1_000_000) .reduce(0, (a, b) -> a + b );
    long endTime = System.currentTimeMillis();
        System.out.println("(endTime - startTime) = " + (endTime - startTime)); //~27ms
        System.out.println("sum = " + suma);
    startTime = System.currentTimeMillis();
    int sum = 0;
        for (int i = 1; i <= 1_000_000; i++) {
        sum += i;
    }
    endTime = System.currentTimeMillis();
        System.out.println("(endTime - startTime) = " + (endTime - startTime));//~4ms
        System.out.println("sum = " + sum);*/

}
