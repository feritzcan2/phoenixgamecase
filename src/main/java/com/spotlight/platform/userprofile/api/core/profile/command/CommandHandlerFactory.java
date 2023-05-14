package com.spotlight.platform.userprofile.api.core.profile.command;

import com.spotlight.platform.userprofile.api.core.profile.command.handlers.CollectCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.ICommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.IncrementCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.ReplaceCommandHandler;
import com.spotlight.platform.userprofile.api.model.command.primitives.CommandType;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class CommandHandlerFactory {
    private Map<CommandType, ICommandHandler> commandStrategies;
    @Inject
    public CommandHandlerFactory(IncrementCommandHandler incrementCommandHandler,
                                 ReplaceCommandHandler replaceCommandHandler,
                                 CollectCommandHandler collectCommandHandler){
        commandStrategies = new HashMap<>();
        commandStrategies.put(CommandType.increment,incrementCommandHandler);
        commandStrategies.put(CommandType.replace,replaceCommandHandler);
        commandStrategies.put(CommandType.collect,collectCommandHandler);
    }
    public CommandHandlerFactory(){
        commandStrategies = new HashMap<>();
    }

    public ICommandHandler  getHandler(CommandType type){
        return commandStrategies.get(type);
    }
}
