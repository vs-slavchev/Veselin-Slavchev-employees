package com.slavchev.employees.employees_service.model;

import com.slavchev.employees.employees_service.exceptions.InvalidCsvException;
import com.slavchev.employees.employees_service.util.TimeUtil;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeeAssignment {

    private final String employeeId;
    private final String projectId;
    private final LocalDate start;
    private final LocalDate end;

    private EmployeeAssignment(String employeeId, String projectId,
                               LocalDate start, LocalDate end) {
        this.employeeId = employeeId;
        this.projectId = projectId;
        this.start = start;
        this.end = end;
    }

    public static EmployeeAssignment fromString(String line) {
        String[] parts = line.split(",");
        try {
            if (parts.length != 4) {
                throw new InvalidCsvException(
                        String.format("Unexpected number of values (%d)", parts.length));
            }

            LocalDate startDate = parseDate(cleanUpField(parts[2]));
            LocalDate endDate = parseDate(cleanUpField(parts[3]));
            if (startDate.isAfter(endDate)) {
                throw new InvalidCsvException(
                        "Start date cannot be after end date");
            }

            return new EmployeeAssignment(
                    cleanUpField(parts[0]),
                    cleanUpField(parts[1]),
                    startDate,
                    endDate);
        } catch (InvalidCsvException e) {
            throw new InvalidCsvException(String.format("%s in line: \"%s\"",
                    e.getMessage(), line), e);
        }
    }

    private static LocalDate parseDate(String stringDate) {
        return stringDate.equals("NULL") ? LocalDate.now() : TimeUtil.parseDate(stringDate);
    }

    private static String cleanUpField(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new InvalidCsvException("Cell value is empty");
        }
        return text.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeAssignment that = (EmployeeAssignment) o;
        return Objects.equals(employeeId, that.employeeId) && Objects.equals(projectId, that.projectId) && Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, projectId, start, end);
    }
}
