package com.slavchev.employees.employees_service;

import com.slavchev.employees.employees_service.exceptions.InvalidCsvException;
import com.slavchev.employees.employees_service.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    public void test_whenUploadingValidCsv_thenHandleAndProcessSuccessfully() throws Exception {
        String input = """
                143, 12, 2000-01-01, 2000-01-05
                218, 12, 2000-01-03, 2000-01-09
                """;
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", input.getBytes());

        this.mvc.perform(multipart("/api/v1/employees/work/assignments").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(this.employeeService).parseAssignments(input);
        verify(this.employeeService).findLongestTeamPeriod(any(Set.class));
    }

    @Test
    public void test_whenNoFileUploaded_then404() throws Exception {
        when(this.employeeService.parseAssignments(any()))
                .thenThrow(new InvalidCsvException("File not found"));
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "".getBytes());

        this.mvc.perform(multipart("/api/v1/employees/work/assignments").file(multipartFile))
                .andExpect(status().isBadRequest());
    }
}
