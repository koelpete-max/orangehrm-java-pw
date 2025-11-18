package com.example.pages.main.pim;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Getter
@Slf4j
public class EmployeeData {

    private final String id;
    private final String firstMiddleName;
    private final String lastName;
    private final String jobTitle;

    public EmployeeData(String id, String firstMiddleName, String lastName, String jobTitle) {
        this.id = id;
        this.firstMiddleName = firstMiddleName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
    }

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
