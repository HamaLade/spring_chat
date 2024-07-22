package com.hs.application.room.service;

import com.hs.persistence.entity.chatroom.Chat;
import com.hs.persistence.entity.chatroom.ChatType;
import com.hs.persistence.entity.chatroom.Room;
import com.hs.persistence.entity.member.Member;
import com.hs.persistence.repository.memeber.MemberRepository;
import com.hs.persistence.repository.room.ChatRepository;
import com.hs.persistence.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;

    // 채팅 메세지 저장
    @Transactional
    public void saveChatMessage(
            ChatType chatType
            , String message
            , Long roomId
            , String senderNickname
            , Boolean hasFiles
    ) {

        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );

        Member member = memberRepository.findByNickname(senderNickname).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        message = classifyMessage(chatType, senderNickname, message);

        Chat chat = new Chat(
                chatType
                , message
                , room
                , senderNickname
                , hasFiles
        );

        chatRepository.save(chat);

    }

    public String classifyMessage(ChatType chatType, String senderNickname, String message) {
        if (chatType.equals(ChatType.INVITATION)) {
            return senderNickname + "님이 초대하였습니다.";
        } else if (chatType.equals(ChatType.LEAVE)) {
            return senderNickname + "님이 나갔습니다.";
        }
        return message;
    }

}
