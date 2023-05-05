package com.spotlight.platform.userprofile.api.core.profile.command;

import com.google.inject.Inject;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.CollectCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.ICommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.IncrementCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.ReplaceCommandHandler;
import com.spotlight.platform.userprofile.api.model.command.primitives.CommandType;

import java.util.HashMap;
import java.util.Map;

public class CommandHandlerFactory {
     private  IncrementCommandHandler incrementCommandHandler;
     private ReplaceCommandHandler replaceCommandHandler;
    private  CollectCommandHandler collectCommandHandler;

    private Map<CommandType, ICommandHandler> commandStrategies;

    public CommandHandlerFactory(){
        commandStrategies = new HashMap<>();
        commandStrategies.put(CommandType.increment,new IncrementCommandHandler());
        commandStrategies.put(CommandType.replace,new ReplaceCommandHandler());
        commandStrategies.put(CommandType.collect,new CollectCommandHandler());
    }

    public ICommandHandler  getHandler(CommandType type){
        return commandStrategies.get(type);
    }
}
