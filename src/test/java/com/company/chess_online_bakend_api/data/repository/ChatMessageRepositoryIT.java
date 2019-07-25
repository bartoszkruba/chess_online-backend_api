package com.company.chess_online_bakend_api.data.repository;

import com.company.chess_online_bakend_api.data.model.ChatMessage;
import com.company.chess_online_bakend_api.data.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("dev")
class ChatMessageRepositoryIT {

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    void setUp() {
        roomRepository.deleteAll();
        chatMessageRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
        chatMessageRepository.deleteAll();
    }

    @Test
    void findByRoom() {

        ChatMessage chatMessage1 = ChatMessage.builder().message("message1").build();

        ChatMessage chatMessage2 = ChatMessage.builder().message("message2").build();

        ChatMessage chatMessage3 = ChatMessage.builder().message("message3").build();

        Room room = Room.builder().name("Alpha").build();

        room.addChatMessage(chatMessage1);
        room.addChatMessage(chatMessage2);
        room.addChatMessage(chatMessage3);

        room = roomRepository.save(room);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.by("created").descending());

        Page<ChatMessage> messagePage = chatMessageRepository.findByRoom(room, pageRequest);

        assertEquals(3, messagePage.get().count());
        assertEquals(1, messagePage.getTotalPages());
        assertEquals(3, messagePage.getTotalElements());

        List<ChatMessage> chatMessages = messagePage.get().collect(Collectors.toList());

        assertEquals("message1", chatMessages.get(0).getMessage());
        assertEquals("message2", chatMessages.get(1).getMessage());
        assertEquals("message3", chatMessages.get(2).getMessage());
    }

    @Test
    void countByRoom() {
        ChatMessage chatMessage1 = ChatMessage.builder().message("message1").build();

        ChatMessage chatMessage2 = ChatMessage.builder().message("message2").build();

        ChatMessage chatMessage3 = ChatMessage.builder().message("message3").build();

        Room room = Room.builder().name("Alpha").build();

        room.addChatMessage(chatMessage1);
        room.addChatMessage(chatMessage2);
        room.addChatMessage(chatMessage3);

        room = roomRepository.save(room);

        Long count = chatMessageRepository.countByRoom(room);

        assertEquals(Long.valueOf(3), count);
    }
}