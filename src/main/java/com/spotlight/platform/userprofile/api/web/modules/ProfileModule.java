package com.spotlight.platform.userprofile.api.web.modules;

import com.google.inject.AbstractModule;

import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.core.profile.command.CommandHandlerFactory;
import com.spotlight.platform.userprofile.api.core.profile.command.ProfileCommandProcessor;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.CollectCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.IncrementCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.command.handlers.ReplaceCommandHandler;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDaoInMemory;

import javax.inject.Singleton;

public class ProfileModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserProfileDao.class).to(UserProfileDaoInMemory.class).in(Singleton.class);
        bind(UserProfileService.class).in(Singleton.class);
        bind(CommandHandlerFactory.class).in(Singleton.class);
        bind(ProfileCommandProcessor.class).in(Singleton.class);
    }
}
