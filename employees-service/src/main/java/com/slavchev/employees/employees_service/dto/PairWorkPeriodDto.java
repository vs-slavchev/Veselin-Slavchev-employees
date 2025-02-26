package com.slavchev.employees.employees_service.dto;

import com.slavchev.employees.employees_service.model.PairWorkPeriod;

public record PairWorkPeriodDto(String employee1Id, String employee2Id, String projectId, int daysWorked) {

    public static PairWorkPeriodDto from(PairWorkPeriod pairWorkPeriod) {
        return new PairWorkPeriodDto(
                pairWorkPeriod.employee1Id(),
                pairWorkPeriod.employee2Id(),
                pairWorkPeriod.projectId(),
                pairWorkPeriod.daysWorked()
        );
    }
}
