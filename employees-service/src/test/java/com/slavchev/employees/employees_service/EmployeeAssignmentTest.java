package com.slavchev.employees.employees_service;

import com.slavchev.employees.employees_service.exceptions.InvalidCsvException;
import com.slavchev.employees.employees_service.model.EmployeeAssignment;
import com.slavchev.employees.employees_service.service.EmployeeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

public class EmployeeAssignmentTest {

    @Test
    public void test_whenDataIsCorrect_thenAssignmentIsParsed() {
        String input = "143, 12, 2013-11-01, 2014-01-05";

        EmployeeAssignment assignment = EmployeeAssignment.fromString(input);

        assertEquals("143", assignment.getEmployeeId());
        assertEquals("12", assignment.getProjectId());
        assertEquals(LocalDate.parse("2013-11-01"), assignment.getStart());
        assertEquals(LocalDate.parse("2014-01-05"), assignment.getEnd());
    }

    @Test
    public void test_whenEndDateIsNull_thenEndDateIsNow() {
        String input = "218, 10, 2012-05-16, NULL";

        EmployeeAssignment assignment = EmployeeAssignment.fromString(input);

        assertEquals(LocalDate.now(), assignment.getEnd());
    }

    @Test
    public void test_whenInputIs4Lines_thenParse4Lines() {
        String input = """
                143, 12, 2013-11-01, 2014-01-05
                218, 10, 2012-05-16, NULL
                143, 10, 2009-01-01, 2011-04-27
                001, 1, 2012-05-22, 2012-05-24
                """;
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assertEquals(4, assignments.size());
    }

    @Test
    public void test_whenInputIs0Lines_thenParse0Lines() {
        String input = "";
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assertTrue(assignments.isEmpty());
    }

    @Test
    public void test_whenInputIs1Line_thenParse1Line() {
        String input = "143, 12, 2013-11-01, 2014-01-05";
        List<EmployeeAssignment> assignments = new EmployeeService().parseAssignments(input);

        assertEquals(1, assignments.size());
    }

    @Test
    public void test_whenStartDateIsAfterEndDate_thenThrowException() {
        String input = "143, 12, 2018-01-01, 2014-01-01";
        assertThrows(InvalidCsvException.class, () -> new EmployeeService().parseAssignments(input));
    }
}
