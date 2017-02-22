package org.egov.user.domain.service;

import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    private final Long ID = 1L;
    private final String EMAIL = "email@gmail.com";
    private final String USER_NAME = "userName";

    @Before
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void shouldGetUserById() throws Exception {
        when(userRepository.findOne(ID)).thenReturn(getUserObject());

        User user = userService.getUserById(ID);

        assertThat(user.getId()).isEqualTo(ID);
    }

    @Test
    public void shouldGetUserByEmail() throws Exception {
        when(userRepository.findByEmailId(EMAIL)).thenReturn(getUserObject());

        User actualUser = userService.getUserByEmailId(EMAIL);

        assertThat(actualUser.getEmailId()).isEqualTo(EMAIL);
    }

    @Test
    public void shouldGetUserByUsername() throws Exception {
        when(userRepository.findByUsername(USER_NAME)).thenReturn(getUserObject());

        User actualUser = userService.getUserByUsername(USER_NAME);

        assertThat(actualUser.getUsername()).isEqualTo(USER_NAME);
    }

    private User getUserObject() {
        return User.builder()
                .id(ID)
                .emailId(EMAIL)
                .username(USER_NAME)
                .build();
    }
}