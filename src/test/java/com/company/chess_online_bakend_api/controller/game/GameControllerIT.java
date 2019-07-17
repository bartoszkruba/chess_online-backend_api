package com.company.chess_online_bakend_api.controller.game;

import com.company.chess_online_bakend_api.bootstrap.dev.RoomBootstrap;
import com.company.chess_online_bakend_api.bootstrap.dev.UserBootstrap;
import com.company.chess_online_bakend_api.controller.GameController;
import com.company.chess_online_bakend_api.data.repository.RoleRepository;
import com.company.chess_online_bakend_api.data.repository.RoomRepository;
import com.company.chess_online_bakend_api.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class GameControllerIT {

    MockMvc mockMvc;

    @Autowired
    private UserBootstrap userBootstrap;

    @Autowired
    private RoomBootstrap roomBootstrap;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setup() throws Exception {
        userBootstrap.onApplicationEvent(null);
        roomBootstrap.run();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = UserBootstrap.ADMIN_USERNAME, authorities = UserBootstrap.ROLE_ADMIN)
    void joinGameAsAdmin() throws Exception {

        Long userId = userRepository.findByUsernameLike(UserBootstrap.ADMIN_USERNAME).get().getId();
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String url = GameController.BASE_URL + gameId + "/join/white";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(gameId.intValue())))
                .andExpect(jsonPath("$.whitePlayer.id", equalTo(userId.intValue())));
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = UserBootstrap.ROLE_USER)
    void joinGameAsUser() throws Exception {

        Long userId = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME).get().getId();
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String url = GameController.BASE_URL + gameId + "/join/BLACK";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(gameId.intValue())))
                .andExpect(jsonPath("$.blackPlayer.id", equalTo(userId.intValue())));
    }

    @Test
    void joinGameNotLogged() throws Exception {
        String url = GameController.BASE_URL + 1 + "/join/BLACK";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = UserBootstrap.USER_USERNAME, authorities = UserBootstrap.ROLE_USER)
    void joinGameInvalidColor() throws Exception {
        String url = GameController.BASE_URL + 13 + "/join/orange";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "invalid username", authorities = UserBootstrap.ROLE_USER)
    void joinGameUsernameNotFound() throws Exception {
        Long userId = userRepository.findByUsernameLike(UserBootstrap.USER_USERNAME).get().getId();
        Long gameId = roomRepository.findByNameLike("Alpha").get().getGame().getId();

        String url = GameController.BASE_URL + gameId + "/join/BLACK";

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", equalTo(404)));
    }
}
