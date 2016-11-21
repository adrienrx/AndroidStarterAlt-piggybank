package net.sparkeek.t2c.tests.ui;

import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;

import com.github.polok.localify.LocalifyClient;

import org.frutilla.FrutillaTestRunner;
import org.frutilla.annotations.Frutilla;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import net.sparkeek.t2c.mvp.ActivityMain;
import net.sparkeek.t2c.tests.mock.MockApplication;
import net.sparkeek.t2c.tests.mock.MockModuleRest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;

//@org.junit.Ignore
@RunWith(FrutillaTestRunner.class)
public class TestActivityRepoList extends AbstractRobotiumTestCase<ActivityMain> {

    //region Fields
    private LocalifyClient mLocalifyClient;
    private MockModuleRest mModuleRest;
    private MockWebServer mMockWebServer;
    //endregion

    //region Constructor matching super
    public TestActivityRepoList() {
        super(new ActivityTestRule<>(ActivityMain.class, true, false));
    }
    //endregion

    //region Test lifecycle
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        mModuleRest = MockApplication.sharedMockApplication().getModuleRest();
        mModuleRest.setUp();
        mMockWebServer = mModuleRest.getMockWebServer();

        mLocalifyClient = new LocalifyClient.Builder()
                .withResources(mContextTest.getResources())
                .build();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        try {
            mMockWebServer.shutdown();
        } catch (@NonNull final Exception loException) {
            loException.printStackTrace();
        }
    }
    //endregion

    //region Test methods
    @Frutilla(
            Given = "A single GitHub repo from the API",
            When = "",
            Then = "It should display a repo named \"git-consortium\""
    )
    @Test
    public void test_ListRepos_WithOneRepo_DisplayListWithOnlyThisRepo() {
        Given:
        {
            final String lsOneRepoJSONData = mLocalifyClient.localify().loadRawFile(net.sparkeek.t2c.test.R.raw.repos_octocat);
            final MockResponse loMockResponseWithOneRepo = new MockResponse().setResponseCode(200);
            loMockResponseWithOneRepo.setBody(lsOneRepoJSONData);
            mMockWebServer.enqueue(loMockResponseWithOneRepo);
            try {
                mMockWebServer.start(4000);
            } catch (@NonNull final Exception loException) {
                loException.printStackTrace();
            }
            mActivity = mActivityTestRule.launchActivity(null);
        }

        When:
        {
        }

        Then:
        {
            final boolean lbFoundTheRepo = mSolo.waitForText("git-consortium", 1, 5000L, true);
            assertThat(lbFoundTheRepo).isTrue();
        }
    }

    @Frutilla(
            Given = "A single GitHub repo from the API",
            When = "Click on its name",
            Then = "It should display the detail of this repo"
    )
    @Test
    public void test_ListRepos_ClickOnOneRepo_DisplayDetailWithOnlyThisRepo() {
        Given:
        {
            final String lsOneRepoJSONData = mLocalifyClient.localify().loadRawFile(net.sparkeek.t2c.test.R.raw.repos_octocat);
            final MockResponse loMockResponseWithOneRepo = new MockResponse().setResponseCode(200);
            loMockResponseWithOneRepo.setBody(lsOneRepoJSONData);
            mMockWebServer.enqueue(loMockResponseWithOneRepo);
            try {
                mMockWebServer.start(4000);
            } catch (@NonNull final Exception loException) {
                loException.printStackTrace();
            }
            mActivity = mActivityTestRule.launchActivity(null);
        }

        When:
        {
            mSolo.clickOnText("git-consortium");
        }

        Then:
        {
            mSolo.assertCurrentActivity("should stay on ActivityMain", ActivityMain.class);
            final boolean lbFoundTheRepo = mSolo.waitForText("This repo is for demonstration purposes only.", 1, 5000L, true);
            assertThat(lbFoundTheRepo).isTrue();
        }
    }

    @Frutilla(
            Given = "A single GitHub repo from the API",
            When = "Click on its name and on the back button",
            Then = "It should display the list"
    )
    @Test
    public void test_DetailRepos_ClickOnBack_DisplayListRepos() {
        Given:
        {
            final String lsOneRepoJSONData = mLocalifyClient.localify().loadRawFile(net.sparkeek.t2c.test.R.raw.repos_octocat);
            final MockResponse loMockResponseWithOneRepo = new MockResponse().setResponseCode(200);
            loMockResponseWithOneRepo.setBody(lsOneRepoJSONData);
            mMockWebServer.enqueue(loMockResponseWithOneRepo);
            try {
                mMockWebServer.start(4000);
            } catch (@NonNull final Exception loException) {
                loException.printStackTrace();
            }
            mActivity = mActivityTestRule.launchActivity(null);
        }

        When:
        {
            mSolo.clickOnText("git-consortium");
            mSolo.goBack();
        }

        Then:
        {
            mSolo.assertCurrentActivity("should be on ActivityMain", ActivityMain.class);
            final boolean lbFoundTheRepo = mSolo.waitForText("git-consortium", 1, 5000L, true);
            assertThat(lbFoundTheRepo).isTrue();
        }
    }
    //endregion
}
