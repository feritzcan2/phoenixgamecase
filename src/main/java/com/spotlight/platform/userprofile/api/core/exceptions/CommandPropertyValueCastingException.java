package com.spotlight.platform.userprofile.api.core.exceptions;

import com.spotlight.platform.userprofile.api.model.command.primitives.CommandType;

public class CommandPropertyValueCastingException extends RuntimeException {
    public CommandPropertyValueCastingException(CommandType type) {
        super();
    }
}
