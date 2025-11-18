package com.example.pages.main;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class SidePanel {
    final Page page;
    final String menuItemNameXpath = "//span[@class='oxd-text oxd-text--span oxd-main-menu-item--name'][normalize-space()='MenuItem']";
    final String menuActiveItemXpath = "//*[@class='oxd-main-menu-item active']";

    @Inject
    public SidePanel(Page page) {
        this.page = page;
    }

    public String getMenuActiveItemName() {
        var activeItem = page.locator(menuActiveItemXpath).textContent();
        log.info("Current active menu name is '{}'", activeItem);
        return activeItem;
    }

    public void selectMenuActiveItem(SidePanelItem sidePanelItem) {
        log.info("Selecting menu item {}", sidePanelItem.name());
        var selectedXpath = menuItemNameXpath.replace("MenuItem", sidePanelItem.toString());
        page.locator(selectedXpath).click();
    }
}
