package com.spotlight.platform.userprofile.api.model.command;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotlight.platform.userprofile.api.model.command.primitives.CommandType;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

import java.io.Serializable;
import java.util.Map;

public abstract class ReplaceCommand implements Serializable {

    @JsonProperty
    public UserId userId;
    @JsonProperty
    public CommandType type;
    @JsonProperty
    public Map<UserProfilePropertyName, UserProfilePropertyValue> properties;
}