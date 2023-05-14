package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.helpers.FixtureHelpers;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserProfileFixtures {
    public static final UserId USER_ID = UserId.valueOf("existing-user-id");
    public static final UserId NON_EXISTING_USER_ID = UserId.valueOf("non-existing-user-id");
    public static final UserId INVALID_USER_ID = UserId.valueOf("invalid-user-id-%");

    public static final Instant LAST_UPDATE_TIMESTAMP = Instant.parse("2021-06-01T09:16:36.123Z");

    public static  final UserProfile USER_PROFILE = new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP,
            Map.of(UserProfilePropertyName.valueOf("property1"), UserProfilePropertyValue.valueOf("property1Value")));


    public static  UserProfile GetCommandBaseProfile(){
        return new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP,
                new HashMap<>(Map.of(UserProfilePropertyName.valueOf("property1"), UserProfilePropertyValue.valueOf("property1Value"),
                        UserProfilePropertyName.valueOf("property3"), UserProfilePropertyValue.valueOf(0),
                        UserProfilePropertyName.valueOf("property4"), UserProfilePropertyValue.valueOf(new ArrayList<String>()))));

    }

    public static  final UserProfile USER_PROFILE_INCREMENTED = new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP,
            new HashMap<>(Map.of(UserProfilePropertyName.valueOf("property1"), UserProfilePropertyValue.valueOf("property1Value"),
                    UserProfilePropertyName.valueOf("property3"), UserProfilePropertyValue.valueOf(3),
                    UserProfilePropertyName.valueOf("property4"), UserProfilePropertyValue.valueOf(new ArrayList<String>()))));

    public static  final UserProfile USER_PROFILE_COMMAND_REPLACED = new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP,
            new HashMap<>(Map.of(UserProfilePropertyName.valueOf("property1"), UserProfilePropertyValue.valueOf("property1Value"),
                    UserProfilePropertyName.valueOf("property3"), UserProfilePropertyValue.valueOf(500),
                    UserProfilePropertyName.valueOf("property4"), UserProfilePropertyValue.valueOf(new ArrayList<String>()))));


    public static  final UserProfile USER_PROFILE_COMMAND_COLLECTED = new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP,
            new HashMap<>(Map.of(UserProfilePropertyName.valueOf("property1"), UserProfilePropertyValue.valueOf("property1Value"),
                    UserProfilePropertyName.valueOf("property3"), UserProfilePropertyValue.valueOf(0),
                    UserProfilePropertyName.valueOf("property4"), UserProfilePropertyValue.valueOf(new ArrayList<String>(
                            Arrays.asList("Test")
                    )))));

    public static final String SERIALIZED_USER_PROFILE = FixtureHelpers.fixture("/fixtures/model/profile/userProfile.json");
    public static final String SERIALIZED_REPLACED_USER_PROFILE = FixtureHelpers.fixture("/fixtures/model/profile/userProfileReplaced.json");

    public static final String SERIALIZED_BATCH_COMMAND_USER_PROFILE = FixtureHelpers.fixture("/fixtures/model/profile/userProfileBatchCommand.json");
}
