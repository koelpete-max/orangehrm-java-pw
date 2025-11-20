package com.example.di;

import com.example.core.TestContext;
import com.example.pages.main.dashboard.DashboardPage;
import com.example.pages.home.HomePage;
import com.example.pages.login.LoginPage;
import com.example.pages.main.admin.AdminPage;
import com.example.pages.main.pim.PimPage;
import dagger.Provides;
import dagger.Module;


@Module
public class PageModule {

    @Provides
    public LoginPage provideLoginPage(TestContext context) {
        return new LoginPage(context.getPage());
    }

    @Provides
    public HomePage provideHomePage(TestContext context) {
        return new HomePage(context.getPage());
    }

    @Provides
    public AdminPage provideAdminPage(TestContext context) {
        return new AdminPage(context.getPage());
    }

    @Provides
    public PimPage providePimPage(TestContext context) {
        return new PimPage(context.getPage());
    }

    @Provides
    public DashboardPage provideDashboardPage(TestContext context) {
        return new DashboardPage(context.getPage());
    }
}