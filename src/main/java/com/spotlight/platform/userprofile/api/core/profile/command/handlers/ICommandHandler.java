package com.spotlight.platform.userprofile.api.core.profile.command.handlers;

import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

public interface ICommandHandler {

     boolean handle(UserProfile profile, UserProfileCommand command);

}
