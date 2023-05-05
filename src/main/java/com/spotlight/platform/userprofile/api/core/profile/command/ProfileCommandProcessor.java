package com.spotlight.platform.userprofile.api.core.profile.command;
import com.google.inject.Inject;
import com.spotlight.platform.userprofile.api.core.exceptions.CommandHandlerNotFoundException;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileCommandProcessor {
    private  CommandHandlerFactory commandHandlerFactory;
    private static final Logger logger =  LoggerFactory.getLogger(ProfileCommandProcessor.class);;

    public ProfileCommandProcessor(){
        commandHandlerFactory = new CommandHandlerFactory();
    }
    public boolean processCommand(UserProfile profile, UserProfileCommand command){

        var commandHandler = commandHandlerFactory.getHandler(command.type());
        if(commandHandler == null) throw new CommandHandlerNotFoundException();
        try{
            return commandHandler.handle(profile,command);

        }catch (Exception e){
            logger.error("Exception in processing command ",e);
            return false;
        }

    }
}
