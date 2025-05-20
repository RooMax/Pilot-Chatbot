package com.aichatbot.pilot.repo;

import com.aichatbot.pilot.model.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
}
