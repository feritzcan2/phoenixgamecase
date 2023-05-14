package com.spotlight.platform.userprofile.api.core.profile.commands;

import com.spotlight.platform.userprofile.api.core.exceptions.CommandHandlerNotFoundException;
import com.spotlight.platform.userprofile.api.core.exceptions.CommandPropertyValueCastingException;
import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.core.profile.command.CommandHandlerFactory;
import com.spotlight.platform.userprofile.api.core.profile.command.ProfileCommandProcessor;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.CollectCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.IncrementCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.ReplaceCommandHandler;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandProcessorTests {
    private final UserProfileDao userProfileDaoMock = mock(UserProfileDao.class);
    private final UserProfileService userProfileService = new UserProfileService(userProfileDaoMock
            ,new ProfileCommandProcessor(new CommandHandlerFactory(new IncrementCommandHandler()
            ,new ReplaceCommandHandler(),new CollectCommandHandler())));
    private final UserProfileService userProfileServiceWithoutHandlers = new UserProfileService(userProfileDaoMock
            ,new ProfileCommandProcessor(new CommandHandlerFactory()));
    @Test
    void missingHandler_throwsException() {
        when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));
        var properties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
        properties.put(UserProfilePropertyName.valueOf("property3"),UserProfilePropertyValue.valueOf(3));
        var command = new UserProfileCommand( CommandType.replace,properties);

        assertThatThrownBy(() -> userProfileServiceWithoutHandlers.processCommand(UserProfileFixtures.USER_ID, command))
                .isExactlyInstanceOf(CommandHandlerNotFoundException.class);

    }

    @Nested
    @DisplayName("increaseCommand")
    class IncreaseCommandTests {
        @Test
        void increaseCommand_worksCorrectly() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));
            var properties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            properties.put(UserProfilePropertyName.valueOf("property3"),UserProfilePropertyValue.valueOf(3));
            var command = new UserProfileCommand( CommandType.increment,properties);

            assertThat(userProfileService.processCommand(UserProfileFixtures.USER_ID, command)).usingRecursiveComparison()
                    .isEqualTo(UserProfileFixtures.USER_PROFILE_INCREMENTED);

        }

        @Test
        void invalidProperties_throwsCastingException() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));
            var properties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            properties.put(UserProfilePropertyName.valueOf("property3"),UserProfilePropertyValue.valueOf("nonNumeric"));
            var command = new UserProfileCommand( CommandType.increment,properties);
            assertThatThrownBy(() -> userProfileService.processCommand(UserProfileFixtures.USER_ID, command))
                    .isExactlyInstanceOf(CommandPropertyValueCastingException.class);
        }
    }
    @Nested
    @DisplayName("collectCommand")
    class CollectCommandTests {
        @Test
        void collectCommand_worksCorrectly() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));
            var properties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            properties.put(UserProfilePropertyName.valueOf("property4"),UserProfilePropertyValue.valueOf(new ArrayList<String>(
                    Arrays.asList("Test")
            )));
            var command = new UserProfileCommand( CommandType.collect,properties);
            var user = userProfileService.processCommand(UserProfileFixtures.USER_ID, command);
            assertThat(user).usingRecursiveComparison()
                    .isEqualTo(UserProfileFixtures.USER_PROFILE_COMMAND_COLLECTED);
        }
        @Test
        void invalidProperties_throwsCastingException() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));
            var properties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            properties.put(UserProfilePropertyName.valueOf("property4"),UserProfilePropertyValue.valueOf(5));
            var command = new UserProfileCommand( CommandType.collect,properties);
            assertThatThrownBy(() -> userProfileService.processCommand(UserProfileFixtures.USER_ID, command))
                    .isExactlyInstanceOf(CommandPropertyValueCastingException.class);
        }
    }
    @Nested
    @DisplayName("replaceCommand")
    class ReplaceCommandTests {
        @Test
        void replaceCommand_worksCorrectly() {
            when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));
            var properties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            properties.put(UserProfilePropertyName.valueOf("property3"),UserProfilePropertyValue.valueOf(500));

            var command = new UserProfileCommand( CommandType.replace,properties);
            var user = userProfileService.processCommand(UserProfileFixtures.USER_ID, command);
            assertThat(user).usingRecursiveComparison()
                    .isEqualTo(UserProfileFixtures.USER_PROFILE_COMMAND_REPLACED);
        }

    }
}