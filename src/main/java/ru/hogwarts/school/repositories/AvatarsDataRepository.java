package ru.hogwarts.school.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.hogwarts.school.model.Avatar;

public interface AvatarsDataRepository extends PagingAndSortingRepository<Avatar, Integer> {

}
