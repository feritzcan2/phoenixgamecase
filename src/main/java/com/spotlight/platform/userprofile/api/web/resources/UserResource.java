package com.spotlight.platform.userprofile.api.web.resources;

import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.model.command.UserProfileCommand;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users/{userId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserProfileService userProfileService;

    @Inject
    public UserResource(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Path("profile")
    @GET
    public UserProfile getUserProfile(@Valid @PathParam("userId") UserId userId) {
        return userProfileService.get(userId);
    }

    @Path("command")
    @POST
    public UserProfile processCommand(@Valid @PathParam("userId") UserId userId ,@Valid UserProfileCommand command) {
        return userProfileService.processCommand(userId,command);
    }

    @Path("commands")
    @POST
    public UserProfile processCommands(@Valid @PathParam("userId") UserId userId ,@NotNull  @Valid List<UserProfileCommand> commands) {
        return userProfileService.processCommands(userId,commands);
    }
}

