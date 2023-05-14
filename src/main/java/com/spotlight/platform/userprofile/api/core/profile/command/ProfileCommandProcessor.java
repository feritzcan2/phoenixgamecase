package com.spotlight.platform.userprofile.api.core.profile.command;
import com.spotlight.platform.userprofile.api.core.exceptions.CommandHandlerNotFoundException;
import com.spotlight.platform.userprofile.api.core.exceptions.CommandPropertyValueCastingException;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ProfileCommandProcessor {
    private  final CommandHandlerFactory commandHandlerFactory;
    private static final Logger logger =  LoggerFactory.getLogger(ProfileCommandProcessor.class);

    @Inject
    public ProfileCommandProcessor(CommandHandlerFactory commandHandlerFactory){

        this.commandHandlerFactory=commandHandlerFactory;
    }
    public boolean processCommand(UserProfile profile, UserProfileCommand command){

        var commandHandler = commandHandlerFactory.getHandler(command.type());
        if(commandHandler == null) throw new CommandHandlerNotFoundException();
        try{
            return commandHandler.handle(profile,command);

        }catch (ClassCastException classCastException){
            logger.error("Casting exception in processing command ",classCastException);
            throw new CommandPropertyValueCastingException(command.type());
        }
        catch (Exception e){
            logger.error("Exception in processing command ",e);
            throw e;
        }

    }
}
