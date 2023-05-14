package com.spotlight.platform.userprofile.api.web.exceptionmappers;

import com.spotlight.platform.userprofile.api.core.exceptions.CommandPropertyValueCastingException;
import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class CommandPropertyCastingExceptionMapper implements ExceptionMapper<CommandPropertyValueCastingException> {
    @Override
    public Response toResponse(CommandPropertyValueCastingException exception) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
