package com.hs.chat.domain.repository.chat;

import com.hs.chat.domain.model.chat.message.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
