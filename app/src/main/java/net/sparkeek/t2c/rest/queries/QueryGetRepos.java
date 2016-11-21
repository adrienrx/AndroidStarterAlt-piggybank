package net.sparkeek.t2c.rest.queries;

import net.sparkeek.t2c.ApplicationAndroidStarter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;

import net.sparkeek.t2c.bus.event.AbstractEventQueryDidFinish;
import net.sparkeek.t2c.persistence.entities.RepoEntity;
import net.sparkeek.t2c.rest.GitHubService;
import net.sparkeek.t2c.rest.dto.DTORepo;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import retrofit2.Call;
import retrofit2.Response;

@AutoInjector(ApplicationAndroidStarter.class)
public class QueryGetRepos extends AbstractQuery {

    //region Injected fields
    @Inject
    transient GitHubService gitHubService;
    @Inject
    transient EventBus eventBus;
    @Inject
    transient SingleEntityStore<Persistable> dataStore;
    //endregion

    //region Fields
    public final boolean pullToRefresh;
    public final String user;
    transient public List<DTORepo> results;
    //endregion

    //region Constructor matching super
    protected QueryGetRepos(final String psUser, final boolean pbPullToRefresh) {
        super(Priority.MEDIUM);
        user = psUser;
        pullToRefresh = pbPullToRefresh;
    }
    //endregion

    //region Overridden method
    @Override
    public void inject() {
        ApplicationAndroidStarter.sharedApplication().componentApplication().inject(this);
    }

    @Override
    protected void execute() throws Exception {
        inject();

        final Call<List<DTORepo>> loCall = gitHubService.listRepos(user);
        final Response<List<DTORepo>> loExecute = loCall.execute();
        results = loExecute.body();

        final int liDeleted = dataStore.delete(RepoEntity.class).get().value();

        final ArrayList<RepoEntity> lloEntities = new ArrayList<>();
        for (final DTORepo loDTORepo : results) {
            final RepoEntity loRepo = new RepoEntity();
            loRepo.setAvatarUrl(loDTORepo.owner.avatarUrl);
            loRepo.setDescription(loDTORepo.description);
            loRepo.setUid(loDTORepo.id);
            loRepo.setName(loDTORepo.name);
            loRepo.setUrl(loDTORepo.url);
            lloEntities.add(loRepo);
        }
        dataStore.insert(lloEntities).toBlocking().value();
    }

    @Override
    protected void postEventQueryFinished() {
        final EventQueryGetReposDidFinish loEvent = new EventQueryGetReposDidFinish(this, mSuccess, mErrorType, mThrowable, pullToRefresh, results);
        eventBus.post(loEvent);
    }

    @Override
    public void postEventQueryFinishedNoNetwork() {
        final EventQueryGetReposDidFinish loEvent = new EventQueryGetReposDidFinish(this, false, AbstractEventQueryDidFinish.ErrorType.NETWORK_UNREACHABLE, null, pullToRefresh, null);
        eventBus.post(loEvent);
    }
    //endregion

    //region Dedicated EventQueryDidFinish
    public static final class EventQueryGetReposDidFinish extends AbstractEventQueryDidFinish<QueryGetRepos> {
        public final boolean pullToRefresh;
        public final List<DTORepo> results;

        public EventQueryGetReposDidFinish(final QueryGetRepos poQuery, final boolean pbSuccess, final ErrorType poErrorType, final Throwable poThrowable, final boolean pbPullToRefresh, final List<DTORepo> ploResults) {
            super(poQuery, pbSuccess, poErrorType, poThrowable);
            pullToRefresh = pbPullToRefresh;
            results = ploResults;
        }
    }
    //endregion
}
