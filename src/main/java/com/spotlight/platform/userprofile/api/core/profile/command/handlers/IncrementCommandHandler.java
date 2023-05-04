package com.spotlight.platform.userprofile.api.core.profile.command.handlers;

import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

public class IncrementCommandHandler implements ICommandHandler {
    @Override
    public boolean handle(UserProfile profile, UserProfileCommand command) {
       for(var profilePropertyEntry  : command.properties().entrySet()){
           var oldPropertyValue =  (int)profile.userProfileProperties().getOrDefault(profilePropertyEntry.getKey(),UserProfilePropertyValue.valueOf(0)).getValue();
           var increment =  (int) command.properties().get(profilePropertyEntry.getKey()).getValue();
           profile.userProfileProperties().put(profilePropertyEntry.getKey(), UserProfilePropertyValue.valueOf(oldPropertyValue+increment));
       }

       return true;
    }
}
