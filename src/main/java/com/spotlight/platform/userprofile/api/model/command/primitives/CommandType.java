package com.spotlight.platform.userprofile.api.model.command.primitives;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spotlight.platform.userprofile.api.model.common.AlphaNumericalStringWithMaxLength;

public enum CommandType {

    increment,
    replace,
    collect
}
