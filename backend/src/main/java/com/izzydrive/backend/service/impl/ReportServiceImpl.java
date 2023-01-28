package com.izzydrive.backend.service.impl;

import com.google.common.collect.Iterables;
import com.izzydrive.backend.dto.reports.*;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.users.Driver;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.service.ReportService;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final UserService userService;

    private final DrivingService drivingService;

    @Override
    public DrivingReportDTO getDrivingReportForPassenger(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        startDate = startDate.plusHours(1);
        endDate = endDate.plusHours(24);
        List<Driving> filteredDrivings = this.drivingService.getDrivingReportForPassenger(userId, startDate, endDate);
        return this.crateReportData(filteredDrivings, startDate, endDate);
    }

    @Override
    public DrivingReportDTO getDrivingReportForDriver(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        startDate = startDate.plusHours(1);
        endDate = endDate.plusHours(24);
        List<Driving> filteredDrivings = this.drivingService.getDrivingReportForDriver(userId, startDate, endDate);
        return this.crateReportData(filteredDrivings, startDate, endDate);
    }

    @Override
    public DrivingReportDTO getDrivingReportForAdmin(ReportRequestDTO reportDTO) {
        LocalDateTime startDate = reportDTO.getStartDate().plusHours(1);
        LocalDateTime endDate = reportDTO.getEndDate().plusHours(24);
        List<Driving> filteredDrivings = new ArrayList<>();
        List<User> users = userService.findAll();
        for (User user : users) {
            if (user instanceof Driver) {
                filteredDrivings.addAll(this.drivingService.getDrivingReportForDriver(user.getId(), startDate, endDate));
            }
        }
        return this.crateReportData(filteredDrivings, startDate, endDate);
    }

    private DrivingReportDTO crateReportData(List<Driving> filteredDrivings, LocalDateTime startDate, LocalDateTime endDate) {
        ArrayList<DrivingPriceReportItem> priceReportItems = new ArrayList<>();
        ArrayList<DrivingDistanceReportItem> distanceReportItems = new ArrayList<>();
        ArrayList<DrivingNumberReportItem> drivingNumberReportItems = new ArrayList<>();
        int sumDrivingNumber = 0;
        double sumPrice = 0.0;
        double sumDistance = 0.0;
        double numberOfDays = Helper.getDistanceBetweenDays(startDate, endDate);
        LocalDate previousDate = null;
        LocalDateTime nextDay = startDate;

        for (Driving d : filteredDrivings) {
            if (nextDay.toLocalDate().isBefore(d.getStartDate().toLocalDate())) {
                addMissingDates(priceReportItems, distanceReportItems, drivingNumberReportItems, nextDay, d.getStartDate());
            }
            if (previousDate != null && d.getStartDate().toLocalDate().isEqual(previousDate)) {
                DrivingPriceReportItem priceReportItem = Iterables.getLast(priceReportItems);
                priceReportItem.setPrice(priceReportItem.getPrice() + d.getPrice());
                DrivingNumberReportItem drivingNumberReportItem = Iterables.getLast(drivingNumberReportItems);
                drivingNumberReportItem.setNumberOfDrivings(drivingNumberReportItem.getNumberOfDrivings() + 1);
                DrivingDistanceReportItem distanceReportItem = Iterables.getLast(distanceReportItems);
                distanceReportItem.setDistance(distanceReportItem.getDistance() + d.getDistance());
                sumPrice += d.getPrice();
                sumDistance += d.getDistance();
                sumDrivingNumber += 1;
                nextDay = d.getStartDate().plusDays(1);
            } else {
                DrivingPriceReportItem priceReportItem = new DrivingPriceReportItem(Helper.convertDate(d.getStartDate().toLocalDate()), d.getPrice());
                sumPrice += d.getPrice();
                DrivingDistanceReportItem distanceReportItem = new DrivingDistanceReportItem(d.getDistance(), Helper.convertDate(d.getStartDate().toLocalDate()));
                sumDistance += d.getDistance();
                DrivingNumberReportItem drivingNumberReportItem = new DrivingNumberReportItem(1, Helper.convertDate(d.getStartDate().toLocalDate()));
                sumDrivingNumber += 1;
                priceReportItems.add(priceReportItem);
                drivingNumberReportItems.add(drivingNumberReportItem);
                distanceReportItems.add(distanceReportItem);
                nextDay = d.getStartDate().plusDays(1);
            }
            previousDate = d.getStartDate().toLocalDate();
        }

        if (nextDay.toLocalDate().isBefore(endDate.toLocalDate())) {
            addMissingDates(priceReportItems, distanceReportItems, drivingNumberReportItems, nextDay, endDate.plusDays(1));
        }

        DrivingReportDTO report = new DrivingReportDTO();
        report.setDrivingsNumber(drivingNumberReportItems);
        report.setDrivingPrices(priceReportItems);
        report.setDrivingsDistances(distanceReportItems);
        report.setAverageDrivingsNumber(sumDrivingNumber / numberOfDays);
        report.setSumDrivingsNumber(sumDrivingNumber);
        report.setAverageDrivingDistance(sumDistance / numberOfDays);
        report.setSumDrivingDistance(sumDistance);
        report.setAverageDrivingPrice(sumPrice / numberOfDays);
        report.setSumDrivingPrice(sumPrice);
        return report;
    }

    private void addMissingDates(ArrayList<DrivingPriceReportItem> priceReportItems,
                                 ArrayList<DrivingDistanceReportItem> distanceReportItems,
                                 ArrayList<DrivingNumberReportItem> drivingNumberReportItems,
                                 LocalDateTime startDate, LocalDateTime endDate) {
        for (LocalDate date = startDate.toLocalDate(); date.isBefore(endDate.toLocalDate()); date = date.plusDays(1)) {
            DrivingPriceReportItem priceReportItem = new DrivingPriceReportItem(Helper.convertDate(date), 0.0);
            DrivingDistanceReportItem distanceReportItem = new DrivingDistanceReportItem(0.0, Helper.convertDate(date));
            DrivingNumberReportItem drivingNumberReportItem = new DrivingNumberReportItem(0, Helper.convertDate(date));
            priceReportItems.add(priceReportItem);
            drivingNumberReportItems.add(drivingNumberReportItem);
            distanceReportItems.add(distanceReportItem);
        }
    }

}
