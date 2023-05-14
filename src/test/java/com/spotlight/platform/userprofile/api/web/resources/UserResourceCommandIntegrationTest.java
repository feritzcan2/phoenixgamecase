package com.spotlight.platform.userprofile.api.web.resources;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.command.primitives.CommandType;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;
import com.spotlight.platform.userprofile.api.web.UserProfileApiApplication;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.vyarus.dropwizard.guice.test.ClientSupport;
import ru.vyarus.dropwizard.guice.test.jupiter.ext.TestDropwizardAppExtension;

import javax.ws.rs.client.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Execution(ExecutionMode.SAME_THREAD)
class UserResourceCommandIntegrationTest {
    @RegisterExtension
    static TestDropwizardAppExtension APP = TestDropwizardAppExtension.forApp(UserProfileApiApplication.class)
            .randomPorts()
            .hooks(builder -> builder.modulesOverride(new AbstractModule() {
                @Provides
                @Singleton
                public UserProfileDao getUserProfileDao() {
                    return mock(UserProfileDao.class);
                }
            }))
            .randomPorts()
            .create();

    @BeforeEach
    void beforeEach(UserProfileDao userProfileDao) {
        reset(userProfileDao);
    }

    @Nested
    @DisplayName("getUserProfile")
    class GetUserProfile {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/profile".formatted(USER_ID_PATH_PARAM);

        @Test
        void existingUser_correctObjectIsReturned(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE);
        }

        @Test
        void nonExistingUser_returns404(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.empty());

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
        }

        @Test
        void validationFailed_returns400(ClientSupport client) {
            var response = client.targetRest()
                    .path(URL)
                    .resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.INVALID_USER_ID)
                    .request()
                    .get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
        }

        @Test
        void unhandledExceptionOccured_returns500(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenThrow(new RuntimeException("Some unhandled exception"));

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }
    }

    @Nested
    @DisplayName("processCommand")
    class ProcessCommand {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/command".formatted(USER_ID_PATH_PARAM);

        @Test
        void validationFailed_returns400(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

            var command = new UserProfileCommand(CommandType.increment,null);
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request()
                    .post(Entity.json(command));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }

        @Test
        void existingUser_correctObjectIsReturned(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

            var command = new UserProfileCommand(CommandType.increment,new HashMap<>());
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request()
                    .post(Entity.json(command));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
              assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE);
        }

        @Test
        void nonExistingUser_returns200(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

            var command = new UserProfileCommand( CommandType.increment,new HashMap<>());
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request()
                    .post(Entity.json(command));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE);
        }

        @Test
        void updateProfileApi_correctObjectReturned(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));

            var properties = new HashMap<UserProfilePropertyName, UserProfilePropertyValue>();
            properties.put(UserProfilePropertyName.valueOf("property1"),UserProfilePropertyValue.valueOf("propertyReplacedValue"));

            var command = new UserProfileCommand( CommandType.replace,properties);

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request()
                    .post(Entity.json(command));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(UserProfileFixtures.SERIALIZED_REPLACED_USER_PROFILE);
        }

    }

    @Nested
    @DisplayName("processCommands")
    class ProcessCommands {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/commands".formatted(USER_ID_PATH_PARAM);

        @Test
        void validationFailed_returns422(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

            var command = new UserProfileCommand(CommandType.increment,null);
            var command2 = new UserProfileCommand(CommandType.increment,null);
            var commands = Arrays.asList(command,command2);
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request()
                    .post(Entity.json(commands));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }

        @Test
        void existingUser_correctResponseIsReturned(ClientSupport client, UserProfileDao userProfileDao) {
            when(userProfileDao.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.GetCommandBaseProfile()));

            var collectCommandProperties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            collectCommandProperties.put(UserProfilePropertyName.valueOf("property4"),UserProfilePropertyValue.valueOf(new ArrayList<String>(
                    Arrays.asList("Test")
            )));
            var collectCommand = new UserProfileCommand( CommandType.collect,collectCommandProperties);

            var replaceCommandProperties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            replaceCommandProperties.put(UserProfilePropertyName.valueOf("property1"),UserProfilePropertyValue.valueOf("propertyReplacedValue"));
            var replaceCommand = new UserProfileCommand( CommandType.replace,replaceCommandProperties);

            var increaseCommandProperties = new HashMap<UserProfilePropertyName,UserProfilePropertyValue>();
            increaseCommandProperties.put(UserProfilePropertyName.valueOf("property3"),UserProfilePropertyValue.valueOf(5));
            var increaseCommand = new UserProfileCommand( CommandType.increment,increaseCommandProperties);

            var commands = Arrays.asList(increaseCommand,collectCommand,replaceCommand);


            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request()
                    .post(Entity.json(commands));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(UserProfileFixtures.SERIALIZED_BATCH_COMMAND_USER_PROFILE);
        }
    }
}