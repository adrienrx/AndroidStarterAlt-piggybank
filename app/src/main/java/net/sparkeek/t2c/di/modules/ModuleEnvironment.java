package net.sparkeek.t2c.di.modules;

import net.sparkeek.t2c.BuildConfig;
import net.sparkeek.t2c.IEnvironment;

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
