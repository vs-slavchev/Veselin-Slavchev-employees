package com.slavchev.employees.employees_service;

import com.slavchev.employees.employees_service.model.EmployeeAssignment;
import com.slavchev.employees.employees_service.model.PairWorkPeriod;
import com.slavchev.employees.employees_service.service.EmployeeService;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EmployeeServiceTest {
    private final EmployeeService employeeService = new EmployeeService();

    @Test
    public void test_when1OverlapIs2Days_then1WorkPeriodOf2Days() {
        String input = """
                143, 12, 2000-01-01, 2000-01-05
                218, 12, 2000-01-03, 2000-01-09
                """;
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.size() == 1);
        assert (longestTeamPeriods.get(0).daysWorked() == 2);
        assert (longestTeamPeriods.get(0).projectId().equals("12"));
    }

    @Test
    public void test_when2EqualOverlapsOf2Days_then2WorkPeriodsOf2Days() {
        String input = """
                143, 12, 2000-01-01, 2000-01-05
                218, 12, 2000-01-03, 2000-01-09
                555, 12, 2000-01-07, 2000-01-09
                """;
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.size() == 2);
        assert (longestTeamPeriods.get(0).daysWorked() == 2);
    }

    @Test
    public void test_whenNoOverlap_thenNoWorkPeriods() {
        String input = """
                143, 12, 2000-01-01, 2000-01-05
                218, 12, 2000-01-06, 2000-01-09
                """;
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.isEmpty());
    }

    @Test
    public void test_whenLargePeriodContainsSmaller_then1WorkPeriodOf2Days() {
        String input = """
                143, 12, 2000-01-01, 2000-01-10
                218, 12, 2000-01-04, 2000-01-06
                """;
        List<EmployeeAssignment> assignments = employeeService.parseAssignments(input);

        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(assignments);

        assert (longestTeamPeriods.size() == 1);
        assert (longestTeamPeriods.get(0).daysWorked() == 2);
        assert (longestTeamPeriods.get(0).projectId().equals("12"));
    }
}
