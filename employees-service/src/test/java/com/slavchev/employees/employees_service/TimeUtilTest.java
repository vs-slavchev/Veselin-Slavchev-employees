package com.slavchev.employees.employees_service;

import com.slavchev.employees.employees_service.exceptions.InvalidCsvException;
import com.slavchev.employees.employees_service.model.EmployeeAssignment;
import com.slavchev.employees.employees_service.util.TimeUtil;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TimeUtilTest {

    @Test
    public void test_whenOverlapIs1Day_thenReturn1Day() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-04, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assertEquals(1, daysWorked);
    }

    @Test
    public void test_whenOverlapIs1DayReversed_thenReturn1Day() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-04, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment2, assignment1);

        assertEquals(1, daysWorked);
    }

    @Test
    public void test_whenOverlapIs0Days_thenReturn0Days() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-05, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assertEquals(0, daysWorked);
    }

    @Test
    public void test_whenOverlapIsComplete_thenReturnAllDays() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-01, 2000-01-05");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assertEquals(4, daysWorked);
    }

    @Test
    public void test_whenStartsOverlap_thenReturnMinDays() {
        EmployeeAssignment assignment1 = EmployeeAssignment.fromString("143, 12, 2000-01-01, 2000-01-05");
        EmployeeAssignment assignment2 = EmployeeAssignment.fromString("218, 12, 2000-01-01, 2000-01-09");

        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment1, assignment2);

        assertEquals(4, daysWorked);
    }

    @Test
    public void test_timeUtil_whenSameDateInDifferentFormats_thenParseCorrectly() {
        LocalDate date1 = TimeUtil.parseDate("2025-01-31");
        LocalDate date2 = TimeUtil.parseDate("01/31/2025");
        LocalDate date3 = TimeUtil.parseDate("31-01-2025");
        LocalDate date4 = TimeUtil.parseDate("31.01.2025");

        assertEquals(date1, date2);
        assertEquals(date2, date3);
        assertEquals(date3, date4);
    }

    @Test
    public void test_timeUtil_whenDateIsInvalid_thenThrowException() {
        assertThrows(InvalidCsvException.class, () -> TimeUtil.parseDate("2025-31-31"));
    }
}
