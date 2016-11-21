package net.sparkeek.piggybank.di.modules;

import net.sparkeek.piggybank.BuildConfig;
import net.sparkeek.piggybank.IEnvironment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModuleEnvironment {

    @Provides
    @Singleton
    public IEnvironment provideEnvironment() {
        return BuildConfig.ENVIRONMENT;
    }
}
