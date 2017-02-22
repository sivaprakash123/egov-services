package org.egov.user.persistence.repository;


import org.egov.user.persistence.entity.User;
import org.egov.user.persistence.entity.enums.Gender;
import org.egov.user.persistence.entity.enums.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void shouldFetchUserById() {
        User user = userRepository.findOne(1L);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(user.getType()).isEqualTo(UserType.EMPLOYEE);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void shouldFetchUserByName() {
        User user = userRepository.findByUsername("userName1");
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @Sql(scripts = {
            "/sql/clearUserRoles.sql",
            "/sql/clearAddresses.sql",
            "/sql/clearRoles.sql",
            "/sql/clearUsers.sql",
            "/sql/createUser.sql"
    })
    public void shouldFetchUserByEmail() {
        User user = userRepository.findByEmailId("email2@gmail.com");
        assertThat(user.getId()).isEqualTo(2L);
    }
}
