package com.example.core;

import com.example.pages.dashboard.DashboardPage;
import com.example.pages.home.HomePage;
import com.example.pages.login.LoginPage;
import com.example.pages.main.SidePanel;
import com.example.pages.main.TopbarPanel;
import com.example.pages.main.admin.AdminPage;
import com.example.pages.pim.PimPage;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface TestComponent {

    LoginPage loginPage();
    HomePage homePage();
    AdminPage adminPage();
    DashboardPage dashboardPage();
    PimPage pimPage();
    SidePanel sidePanel();
    TopbarPanel topbarPanel();

    @Component.Factory
    interface Factory {
        TestComponent create(@BindsInstance com.microsoft.playwright.Page page);
    }
}
