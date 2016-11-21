package net.sparkeek.t2c.rest.queries;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import net.sparkeek.t2c.bus.event.AbstractEventQueryDidFinish;
import hugo.weaving.DebugLog;

public abstract class AbstractQuery extends Job {

    protected enum Priority {
        LOW(0),
        MEDIUM(500),
        HIGH(1000);
        private final int value;

        Priority(final int piValue) {
            value = piValue;
        }
    }

    protected boolean mSuccess;
    protected Throwable mThrowable;
    protected AbstractEventQueryDidFinish.ErrorType mErrorType;

    //region Protected constructor
    protected AbstractQuery(final Priority poPriority) {
        super(new Params(poPriority.value).requireNetwork());
    }

    protected AbstractQuery(final Priority poPriority, final boolean pbPersistent, final String psGroupId, final long plDelayMs) {
        super(new Params(poPriority.value).requireNetwork().setPersistent(pbPersistent).setGroupId(psGroupId).setDelayMs(plDelayMs));
    }
    //endregion

    //region Overridden methods
    @DebugLog
    @Override
    public void onAdded() {
    }

    @DebugLog
    @Override
    public void onRun() throws Throwable {

        try {
            execute();
            mSuccess = true;
        } catch (Throwable loThrowable) {
            mErrorType = AbstractEventQueryDidFinish.ErrorType.UNKNOWN;
            mThrowable = loThrowable;
            mSuccess = false;
        }

        postEventQueryFinished();
    }

    @Override
    protected void onCancel() {
    }

    @DebugLog
    @Override
    protected int getRetryLimit() {
        return 1;
    }
    //endregion

    //region Protected abstract method for specific job
    public abstract void inject();

    protected abstract void execute() throws Exception;

    protected abstract void postEventQueryFinished();

    public abstract void postEventQueryFinishedNoNetwork();
    //endregion
}
