package com.spotlight.platform.userprofile.api.core.profile.command;
import com.google.inject.Inject;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileCommandProcessor {
    @Inject private  CommandHandlerFactory commandHandlerFactory;
    private static final Logger logger =  LoggerFactory.getLogger(ProfileCommandProcessor.class);;

    public boolean processCommand(UserProfile profile, UserProfileCommand command){

        var commandHandler = commandHandlerFactory.getHandler(command.type());
        if(commandHandler == null) {
            logger.error(String.format("Command handler for %s is not found",command.type()));
            return false;
        }
        return commandHandler.handle(profile,command);
    }
}
