package com.izzydrive.backend.service;

import com.izzydrive.backend.dto.reports.DrivingReportDTO;
import com.izzydrive.backend.dto.reports.ReportRequestDTO;

import java.time.LocalDateTime;

public interface ReportService {

    DrivingReportDTO getDrivingReportForPassenger(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    DrivingReportDTO getDrivingReportForDriver(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    DrivingReportDTO getDrivingReportForAdmin(ReportRequestDTO reportRequestDTO);
}
