package com.example.di;

import com.example.pages.main.dashboard.DashboardPage;
import com.example.pages.home.HomePage;
import com.example.pages.login.LoginPage;
import com.example.pages.main.SidePanel;
import com.example.pages.main.TopbarPanel;
import com.example.pages.main.admin.AdminPage;
import com.example.pages.main.pim.PimPage;
import com.microsoft.playwright.Page;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface TestComponent {

    // Page Objects
    LoginPage loginPage();
    HomePage homePage();
    AdminPage adminPage();
    DashboardPage dashboardPage();
    PimPage pimPage();

    // „Panels“
    SidePanel sidePanel();
    TopbarPanel topbarPanel();

    @Component.Factory
    interface Factory {
        TestComponent create(@BindsInstance Page page);
    }
}
