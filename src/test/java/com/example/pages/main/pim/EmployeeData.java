package com.example.pages.main.pim;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public record EmployeeData(String id, String firstMiddleName, String lastName, String jobTitle) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            log.error("Employee data equals null");
            return false;
        }
        if (getClass() != o.getClass()) {
            log.error("Employee classes are not equal");
            return false;
        }
        EmployeeData that = (EmployeeData) o;

        if (!Objects.equals(id, that.id)) {
            log.error("Employee id not equal {} / {}", id, that.id);
            return false;
        }
        if (!Objects.equals(firstMiddleName.trim(), that.firstMiddleName.trim())) {
            log.error("Employee firstMiddleName not equal {} / {}", firstMiddleName, that.firstMiddleName);
            return false;
        }
        if (!Objects.equals(lastName, that.lastName)) {
            log.error("Employee lastName not equal {} / {}", lastName, that.lastName);
            return false;
        }
        if (!Objects.equals(jobTitle, that.jobTitle)) {
            log.error("Employee jobTitle not equal {} / {}", jobTitle, that.jobTitle);
            return false;
        }

        return true;
    }
}
