package com.gymtracker.diary_log;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryLogRepository extends CrudRepository<DiaryLog, Long> {
}
