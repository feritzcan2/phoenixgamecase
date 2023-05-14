package com.spotlight.platform.userprofile.api.model.command;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotlight.platform.userprofile.api.model.command.primitives.CommandType;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

import javax.validation.constraints.NotNull;
import java.util.Map;

/******** COMMENT

    I would prefer using different classes for each command but since data model is common for now,
    I didnt create different classes for faster deserialization
**********/
public record UserProfileCommand( @NotNull @JsonProperty CommandType type,
                                  @NotNull @JsonProperty Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
}

