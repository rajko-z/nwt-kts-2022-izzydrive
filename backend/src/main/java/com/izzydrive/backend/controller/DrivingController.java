package com.izzydrive.backend.controller;

import com.izzydrive.backend.dto.CancellationReasonDTO;
import com.izzydrive.backend.dto.TextResponse;
import com.izzydrive.backend.dto.driving.*;
import com.izzydrive.backend.dto.map.AddressOnMapDTO;
import com.izzydrive.backend.dto.reports.DrivingReportDTO;
import com.izzydrive.backend.dto.reports.ReportRequestDTO;
import com.izzydrive.backend.service.ReportService;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.cancelation.DrivingCancellationService;
import com.izzydrive.backend.service.driving.execution.DrivingExecutionService;
import com.izzydrive.backend.service.driving.rejection.DrivingRejectionService;
import com.izzydrive.backend.service.driving.searchcurrent.CurrentDrivingsSearch;
import com.izzydrive.backend.service.drivingfinder.regular.DrivingFinderRegularService;
import com.izzydrive.backend.service.drivingfinder.reservation.DrivingFinderReservationService;
import com.izzydrive.backend.service.drivingprocessing.regular.ProcessDrivingRegularService;
import com.izzydrive.backend.service.drivingprocessing.reservation.ProcessDrivingReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/drivings")
@AllArgsConstructor
public class DrivingController {

    private final DrivingService drivingService;

    private final DrivingFinderRegularService drivingFinderRegularService;

    private final DrivingFinderReservationService drivingFinderReservationService;

    private final ProcessDrivingRegularService processDrivingRegularService;

    private final ProcessDrivingReservationService processDrivingReservationService;

    private final DrivingRejectionService drivingRejectionService;

    private final DrivingExecutionService drivingExecutionService;

    private final DrivingCancellationService drivingCancellationService;

    private final CurrentDrivingsSearch currentDrivingsSearch;

    private final ReportService reportService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @GetMapping("driver/{driverId}")
    public ResponseEntity<List<DrivingDTO>> findAllByDriverId(@PathVariable Long driverId) {
        List<DrivingDTO> drivings = drivingService.findAllByDriverId(driverId);
        return new ResponseEntity<>(drivings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("passenger/{passengerId}")
    public ResponseEntity<List<DrivingDTO>> findAllByPassengerId(@PathVariable Long passengerId) {
        List<DrivingDTO> drivings = drivingService.findAllByPassengerId(passengerId);
        return new ResponseEntity<>(drivings, HttpStatus.OK);
    }

    @PostMapping("/finder/simple")
    public ResponseEntity<List<DrivingOptionDTO>> findSimpleDrivings(@RequestBody @Valid List<AddressOnMapDTO> addresses) {
        List<DrivingOptionDTO> retVal = drivingFinderRegularService.getSimpleDrivingOptions(addresses);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping("finder/advanced")
    public ResponseEntity<List<DrivingOptionDTO>> findAdvancedDrivings(@RequestBody @Valid DrivingFinderRequestDTO request) {
        List<DrivingOptionDTO> retVal = drivingFinderRegularService.getAdvancedDrivingOptions(request);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping("finder/schedule")
    public ResponseEntity<List<DrivingOptionDTO>> findScheduleDrivings(@RequestBody @Valid DrivingFinderRequestDTO request) {
        List<DrivingOptionDTO> retVal = drivingFinderReservationService.getScheduleDrivingOptions(request);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping(value = "/process")
    public ResponseEntity<TextResponse> processDrivingRequest(@RequestBody DrivingRequestDTO request) {
        this.processDrivingRegularService.process(request);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @PostMapping(value = "/process-reservation")
    public ResponseEntity<TextResponse> processDrivingReservationRequest(@RequestBody DrivingRequestDTO request) {
        this.processDrivingReservationService.processReservation(request);
        return new ResponseEntity<>(new TextResponse("Success"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping(value = "/reject-linked-user")
    public ResponseEntity<TextResponse> rejectDrivingLinkedUser() {
        drivingRejectionService.rejectDrivingLinkedUser();
        return new ResponseEntity<>(new TextResponse("Successfully denied driving"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("/{id}")
    public ResponseEntity<DrivingDTO> findDrivingById(@PathVariable Long id) {
        DrivingDTO driving = drivingService.findDrivingDTOById(id);
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PASSENGER')")
    @GetMapping("passenger/history/{passengerId}")
    public ResponseEntity<List<DrivingDTO>> getPassengerDrivingHistory(@PathVariable Long passengerId) {
        List<DrivingDTO> drivings = drivingService.getPassengerDrivingHistory(passengerId);
        return new ResponseEntity<>(drivings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @GetMapping("passenger/reservations/{passengerId}")
    public ResponseEntity<List<DrivingDTO>> getPassengerFutureReservations(@PathVariable Long passengerId) {
        List<DrivingDTO> drivings = drivingService.getPassengerFutureReservations(passengerId);
        return new ResponseEntity<>(drivings, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_PASSENGER')")
    @DeleteMapping("passenger/cancel-reservation/{drivingId}")
    public ResponseEntity<TextResponse> cancelReservation(@PathVariable Long drivingId) {
        drivingService.cancelReservation(drivingId);
        return new ResponseEntity<>(new TextResponse("Successfully canceled reservation"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("/start")
    public ResponseEntity<TextResponse> startDriving() {
        drivingExecutionService.startDriving();
        return new ResponseEntity<>(new TextResponse("Driving successfully started"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("/end")
    public ResponseEntity<TextResponse> endDriving() {
        drivingExecutionService.endDriving();
        return new ResponseEntity<>(new TextResponse("Driving successfully ended"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER')")
    @GetMapping("reservation")
    public ResponseEntity<DrivingDTO> getReservation() {
        DrivingDTO driving = drivingService.getReservation();
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("delete/{id}")
    public ResponseEntity<TextResponse> deleteDriving(@PathVariable Long id) {
        drivingService.deleteDriving(id);
        return new ResponseEntity<>(new TextResponse("Successfully delete driving"), HttpStatus.OK);
    }

    @PostMapping("/reject-regular-driver")
    public ResponseEntity<TextResponse> rejectRegularDrivingDriver(@RequestBody CancellationReasonDTO cancellationReasonDTO) {
        drivingCancellationService.cancelRegularDriving(cancellationReasonDTO);
        return new ResponseEntity<>(new TextResponse("Successfully denied driving"), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PASSENGER')")
    @PostMapping("/reports-passenger")
    public ResponseEntity<DrivingReportDTO> generateDrivingReportForPassenger(@RequestBody ReportRequestDTO reportRequestDTO) {
        DrivingReportDTO report = this.reportService.getDrivingReportForPassenger(reportRequestDTO.getUserId(), reportRequestDTO.getStartDate(), reportRequestDTO.getEndDate());
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @PostMapping("/reports-driver")
    public ResponseEntity<DrivingReportDTO> generateDrivingReportForDriver(@RequestBody ReportRequestDTO reportRequestDTO) {
        DrivingReportDTO report = this.reportService.getDrivingReportForDriver(reportRequestDTO.getUserId(), reportRequestDTO.getStartDate(), reportRequestDTO.getEndDate());
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/reports-admin")
    public ResponseEntity<DrivingReportDTO> generateDrivingReportForAdmin(@RequestBody ReportRequestDTO reportRequestDTO) {
        DrivingReportDTO report = this.reportService.getDrivingReportForAdmin(reportRequestDTO);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all-current")
    public ResponseEntity<List<DrivingDTOWithLocations>> getAllCurrentDrivings() {
        List<DrivingDTOWithLocations> retVal = currentDrivingsSearch.getAllCurrentDrivings();
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all-current/{term}")
    public ResponseEntity<List<DrivingDTOWithLocations>> getAllCurrentDrivingsBySearchTerm(@PathVariable String term) {
        List<DrivingDTOWithLocations> retVal = currentDrivingsSearch.getAllCurrentDrivingsBySearchTerm(term);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/with-locations/{id}")
    public ResponseEntity<DrivingDTOWithLocations> findDrivingWithLocationsById(@PathVariable Long id) {
        DrivingDTOWithLocations driving = drivingService.findDrivingWithLocationsById(id);
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PASSENGER', 'ROLE_DRIVER')")
    @GetMapping("/finished/{id}")
    public ResponseEntity<FinishedDrivingDetailsDTO> findFinishedDrivingDetailsById(@PathVariable Long id) {
        FinishedDrivingDetailsDTO driving = drivingService.findFinishedDrivingDetailsById(id);
        return new ResponseEntity<>(driving, HttpStatus.OK);
    }
}
