package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.SelectTime;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Size;
import java.util.Optional;

public interface SelectTimeRepository extends JpaRepository<SelectTime, Integer> {
    Optional<SelectTime> findSelectTimeByTimeName(@Size(max = 20) String timeName);
}
