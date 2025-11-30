package com.example.di;

import com.example.base.TestUser;
import com.example.utils.EnvConfig;
import com.example.testdata.TestUserProvider;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class ConfigModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    String provideBaseUrl() {
        return EnvConfig.resolveBaseUrl();
    }

    @Provides
    @Singleton
    TestUser provideDefaultTestUser()  {
        return TestUserProvider.defaultAdmin();
    }
}