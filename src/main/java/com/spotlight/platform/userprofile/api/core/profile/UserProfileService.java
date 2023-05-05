package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.command.ProfileCommandProcessor;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserProfileService {
    private final UserProfileDao userProfileDao;
    private final ProfileCommandProcessor commandProcessor;



    @Inject
    public UserProfileService(UserProfileDao userProfileDao,ProfileCommandProcessor commandProcessor) {
        this.userProfileDao = userProfileDao;
        this.commandProcessor = commandProcessor;
    }

    public UserProfile get(UserId userId) {
        return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
    }

    public UserProfile processCommand(UserProfileCommand command) {
        var user = userProfileDao.get(command.userId()).orElse(new UserProfile(command.userId(), Instant.now(),new HashMap<>()));
        commandProcessor.processCommand(user,command);
        userProfileDao.put(user);
        return user;
    }

    public UserProfile processCommands(List<UserProfileCommand> commands) {
        if(commands.isEmpty()) return null;
        var userId = commands.get(0).userId();
        var user = userProfileDao.get(userId).orElse(new UserProfile(userId, Instant.now(),new HashMap<>()));
        for (var command:commands) {
            commandProcessor.processCommand(user,command);
            userProfileDao.put(user);
        }

        return user;
    }
}
