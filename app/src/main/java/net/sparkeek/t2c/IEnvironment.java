package net.sparkeek.t2c;

import okhttp3.logging.HttpLoggingInterceptor;

public interface IEnvironment {

    HttpLoggingInterceptor.Level getHttpLoggingInterceptorLevel();

    boolean isDebugDrawerEnabled();

}
