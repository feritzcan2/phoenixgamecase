package com.spotlight.platform.userprofile.api.core.profile.command.handlers;

import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectCommandHandler implements ICommandHandler {
    @Override
    public boolean handle(UserProfile profile, UserProfileCommand command) {
       for(var profilePropertyEntry  : command.properties().entrySet()){
           try{
               var oldPropertyValue =  (List<String>) (profile.userProfileProperties().getOrDefault(profilePropertyEntry.getKey(), UserProfilePropertyValue.valueOf(new ArrayList<String>())).getValue());
               var increment = (List<String>) command.properties().get(profilePropertyEntry.getKey()).getValue();

               oldPropertyValue.addAll(increment);
           }catch (Exception e){
               System.out.println("a");
           }
       }
       return true;
    }
}
