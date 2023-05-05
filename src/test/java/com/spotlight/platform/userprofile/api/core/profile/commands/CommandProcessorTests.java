package com.spotlight.platform.userprofile.api.core.profile.commands;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.core.profile.command.ProfileCommandProcessor;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.command.primitives.CommandType;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandProcessorTests {
    private final UserProfileDao userProfileDaoMock = mock(UserProfileDao.class);
    private final UserProfileService userProfileService = new UserProfileService(userProfileDaoMock,new ProfileCommandProcessor());

    @Nested
    @DisplayName("post")
    class Get {
        @Test
        void increaseCommand_worksCorrectly() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE_COMMAND));
            var properties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            properties.put(UserProfilePropertyName.valueOf("property3"),UserProfilePropertyValue.valueOf(3));
            var command = new UserProfileCommand(UserProfileFixtures.USER_ID, CommandType.increment,properties);

            assertThat(userProfileService.processCommand(command)).usingRecursiveComparison()
                    .isEqualTo(UserProfileFixtures.USER_PROFILE_INCREMENTED);
        }

    }
}