package net.sparkeek.t2c;

import android.app.Application;
import android.os.StrictMode;

import com.novoda.merlin.Merlin;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import net.sparkeek.t2c.di.modules.ModuleAsync;
import net.sparkeek.t2c.di.modules.ModuleBus;
import net.sparkeek.t2c.di.modules.ModuleContext;
import net.sparkeek.t2c.di.modules.ModuleDatabase;
import net.sparkeek.t2c.di.modules.ModuleEnvironment;
import net.sparkeek.t2c.di.modules.ModuleRest;

@AutoComponent(
        modules = {
                ModuleAsync.class,
                ModuleBus.class,
                ModuleContext.class,
                ModuleDatabase.class,
                ModuleEnvironment.class,
                ModuleRest.class,
        }
)
@Singleton
@AutoInjector(ApplicationAndroidStarter.class)
public class ApplicationAndroidStarter extends Application {
    private static final String TAG = ApplicationAndroidStarter.class.getSimpleName();

    //region Singleton
    protected static ApplicationAndroidStarter sSharedApplication;

    public static ApplicationAndroidStarter sharedApplication() {
        return sSharedApplication;
    }
    //endregion

    //region Fields (components)
    protected ApplicationAndroidStarterComponent mComponentApplication;
    //endregion

    //region Injected fields
    @Inject
    Merlin merlin;
    //endregion

    //region Overridden methods
    @Override
    public void onCreate() {
        super.onCreate();
        sSharedApplication = this;

        Logger.init(TAG)
                .logLevel(LogLevel.FULL);

        buildComponent();

        mComponentApplication.inject(this);
        merlin.bind();

        final StrictMode.ThreadPolicy loStrictModeThreadPolicy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyDeath()
                .build();
        StrictMode.setThreadPolicy(loStrictModeThreadPolicy);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sSharedApplication = null;
        merlin.unbind();
    }
    //endregion

    //region Getters
    public ApplicationAndroidStarterComponent componentApplication() {
        return mComponentApplication;
    }
    //endregion

    //region Protected methods
    protected void buildComponent() {
        mComponentApplication = DaggerApplicationAndroidStarterComponent.builder()
                .moduleAsync(new ModuleAsync())
                .moduleBus(new ModuleBus())
                .moduleContext(new ModuleContext(getApplicationContext()))
                .moduleDatabase(new ModuleDatabase())
                .moduleEnvironment(new ModuleEnvironment())
                .moduleRest(new ModuleRest())
                .build();
    }
    //endregion
}
