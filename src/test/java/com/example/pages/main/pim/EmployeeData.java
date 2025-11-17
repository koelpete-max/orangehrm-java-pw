package com.example.pages.pim;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Getter
@Setter
@Slf4j
public class EmployeeData {
    private String id;
    private String firstMiddleName;
    private String lastName;
    private String jobTitle;

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
            log.error("Employee data equals id  {} / {}", id, that.id);
            return false;
        }
        if (!Objects.equals(firstMiddleName.trim(), that.firstMiddleName.trim())) {
            log.error("Employee data equals firstMiddleName {} / {}", firstMiddleName, that.firstMiddleName);
            return false;
        }
        if (!Objects.equals(lastName, that.lastName)) {
            log.error("Employee data equals lastName  {} / {}", lastName, that.lastName);
            return false;
        }
        if (!Objects.equals(jobTitle, that.jobTitle)) {
            log.error("Employee data equals jobTitle   {} / {}", jobTitle, that.jobTitle);
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstMiddleName, lastName, jobTitle);
    }
}
