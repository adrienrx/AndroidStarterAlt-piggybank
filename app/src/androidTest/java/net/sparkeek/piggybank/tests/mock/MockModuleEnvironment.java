package net.sparkeek.piggybank.tests.mock;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import net.sparkeek.piggybank.Environment;
import net.sparkeek.piggybank.di.modules.ModuleEnvironment;

@Module
public class MockModuleEnvironment extends ModuleEnvironment {

    @Provides
    @Singleton
    public Environment provideEnvironment() {
        return Environment.TEST;
    }
}
