package com.spotlight.platform.userprofile.api.core.profile.command.handlers;

import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

public class ReplaceCommandHandler implements  ICommandHandler {

    @Override
    public boolean handle(UserProfile profile, UserProfileCommand command) {
       for(var profilePropertyEntry  : command.properties().entrySet()){
           profile.userProfileProperties().put(profilePropertyEntry.getKey(),profilePropertyEntry.getValue());
       }

       return true;
    }
}
