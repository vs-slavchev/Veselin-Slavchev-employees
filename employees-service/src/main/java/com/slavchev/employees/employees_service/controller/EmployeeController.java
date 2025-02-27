package com.slavchev.employees.employees_service.controller;

import com.slavchev.employees.employees_service.dto.PairWorkPeriodDto;
import com.slavchev.employees.employees_service.model.EmployeeAssignment;
import com.slavchev.employees.employees_service.model.PairWorkPeriod;
import com.slavchev.employees.employees_service.service.CsvService;
import com.slavchev.employees.employees_service.service.EmployeeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@CrossOrigin
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final CsvService csvService;

    public EmployeeController(EmployeeService employeeService, CsvService csvService) {
        this.employeeService = employeeService;
        this.csvService = csvService;
    }

    @PostMapping(value = "/work/assignments",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PairWorkPeriodDto>> processWorkAssignments(
            @RequestParam("file") MultipartFile file) {
        String csvContent = csvService.handleUpload(file);

        Set<EmployeeAssignment> employeeAssignments = employeeService.parseAssignments(csvContent);
        List<PairWorkPeriod> longestTeamPeriods = employeeService.findLongestTeamPeriod(employeeAssignments);

        List<PairWorkPeriodDto> dtos = longestTeamPeriods.stream()
                .map(PairWorkPeriodDto::from)
                .toList();
        return ResponseEntity.ok().body(dtos);
    }

}
