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
import java.util.Map;
import java.util.Optional;

public class UserProfileService {
    private final UserProfileDao userProfileDao;

    @Inject
    ProfileCommandProcessor commandProcessor;

    @Inject
    public UserProfileService(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    public UserProfile get(UserId userId) {
        return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
    }

    public UserProfile processCommand(UserProfileCommand command) {
        var user = userProfileDao.get(command.userId());
        if(user.isEmpty())  user = Optional.of(new UserProfile(command.userId(), Instant.now(),new HashMap<>()));
        commandProcessor.processCommand(user.get(),command);
        userProfileDao.put(user.get());
        return user.get();
    }
}
