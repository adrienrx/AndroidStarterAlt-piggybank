package net.sparkeek.t2c.tests.mock;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import net.sparkeek.t2c.Environment;
import net.sparkeek.t2c.di.modules.ModuleEnvironment;

@Module
public class MockModuleEnvironment extends ModuleEnvironment {

    @Provides
    @Singleton
    public Environment provideEnvironment() {
        return Environment.TEST;
    }
}
