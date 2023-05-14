package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.command.ProfileCommandProcessor;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

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

    public UserProfile processCommand(UserId userId, UserProfileCommand command) {
        var user = userProfileDao.get(userId).orElse(new UserProfile(userId, Instant.now(),new HashMap<>()));
        var updated = commandProcessor.processCommand(user,command);
        if(!updated) return null;
        userProfileDao.put(user);
        return user;
    }

    public UserProfile processCommands(UserId userId,List<UserProfileCommand> commands) {
        if(commands.isEmpty()) return null;
        var user = userProfileDao.get(userId).orElse(new UserProfile(userId, Instant.now(),new HashMap<>()));
        for (var command:commands) {
            var status = commandProcessor.processCommand(user,command);
            if(status == false) return null;
        }
        userProfileDao.put(user);
        return user;
    }
}
