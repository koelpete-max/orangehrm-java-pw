package com.example.modules;

import com.example.core.TestContext;
import com.example.pages.dashboard.DashboardPage;
import com.example.pages.home.HomePage;
import com.example.pages.login.LoginPage;
import com.example.pages.main.admin.AdminPage;
import com.example.pages.pim.PimPage;
import dagger.Provides;

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