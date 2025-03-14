package com.slavchev.employees.employees_service.service;

import com.slavchev.employees.employees_service.model.EmployeeAssignment;
import com.slavchev.employees.employees_service.model.PairWorkPeriod;
import com.slavchev.employees.employees_service.util.TimeUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    public Set<EmployeeAssignment> parseAssignments(String input) {
        String[] lines = input.split("\n");
        if (lines.length <= 1 && lines[0].isBlank()) {
            return Set.of();
        }
        return Arrays.stream(lines)
                .map(EmployeeAssignment::fromString)
                .collect(Collectors.toSet());
    }

    public List<PairWorkPeriod> findLongestTeamPeriod(Set<EmployeeAssignment> assignments) {
        Map<String, List<EmployeeAssignment>> assignmentsToProjects =
                assignments.stream().collect(Collectors.groupingBy(EmployeeAssignment::getProjectId));
        List<PairWorkPeriod> longestTeamPeriods = new ArrayList<>();

        for (List<EmployeeAssignment> assignmentsInSameProject : assignmentsToProjects.values()) {
            for (int i = 0; i < assignmentsInSameProject.size(); i++) {
                EmployeeAssignment assignment = assignmentsInSameProject.get(i);
                for (int j = i + 1; j < assignmentsInSameProject.size(); j++) {
                    EmployeeAssignment otherAssignment = assignmentsInSameProject.get(j);
                    // same employee should not be checked against itself
                    if (assignment.getEmployeeId().equals(otherAssignment.getEmployeeId())) {
                        continue;
                    }

                    updateLongestTeamPeriod(assignment, otherAssignment, longestTeamPeriods);
                }
            }
        }
        return longestTeamPeriods;
    }

    private static void updateLongestTeamPeriod(EmployeeAssignment assignment, EmployeeAssignment otherAssignment, List<PairWorkPeriod> longestTeamPeriods) {
        int daysWorked = TimeUtil.getOverlappingTimePeriod(assignment, otherAssignment);
        int maximumPeriod = longestTeamPeriods.isEmpty() ? 0 : longestTeamPeriods.get(0).daysWorked();
        if (daysWorked >= maximumPeriod && daysWorked > 0) {
            PairWorkPeriod newLongestWorkPeriod = new PairWorkPeriod(
                    assignment.getEmployeeId(), otherAssignment.getEmployeeId(),
                    assignment.getProjectId(), daysWorked);
            if (daysWorked > maximumPeriod) {
                longestTeamPeriods.clear();
            }
            longestTeamPeriods.add(newLongestWorkPeriod);
        }
    }
}
