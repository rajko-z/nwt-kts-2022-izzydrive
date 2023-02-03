package com.izzydrive.backend.service.notification;


import com.izzydrive.backend.dto.CarDTO;
import com.izzydrive.backend.dto.NotificationDTO;
import com.izzydrive.backend.dto.UserDTO;
import com.izzydrive.backend.exception.NotFoundException;
import com.izzydrive.backend.model.Address;
import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.model.Notification;
import com.izzydrive.backend.model.NotificationStatus;
import com.izzydrive.backend.model.users.Passenger;
import com.izzydrive.backend.model.users.User;
import com.izzydrive.backend.repository.NotificationRepository;
import com.izzydrive.backend.service.users.UserService;
import com.izzydrive.backend.service.users.admin.AdminService;
import com.izzydrive.backend.utils.ExceptionMessageConstants;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final NotificationRepository notificationRepository;

    private final UserService userService;

    private final AdminService adminService;

    @Override
    public void sendNotificationNewReservationDriving(String email, Driving driving) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have a new reservation");
        notificationDTO.setDuration(driving.getDuration());
        notificationDTO.setPrice(driving.getPrice());
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        List<String> intermediateStationDTO = new ArrayList<>();
        for (Address intermediateStation : driving.getRoute().getIntermediateStations()) {
            intermediateStationDTO.add(intermediateStation.getName());
        }
        notificationDTO.setIntermediateLocations(intermediateStationDTO);
        notificationDTO.setReservationTime(driving.getReservationDate());
        notificationDTO.setUserEmail(email);
        notificationDTO.setDrivingId(driving.getId());
        notificationDTO.setNotificationStatus(NotificationStatus.NEW_RESERVATION);
        this.simpMessagingTemplate.convertAndSend("/notification/newReservationDriving", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationNewDriving(String passengerEmail, Driving driving) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have been added to a new ride");
        notificationDTO.setDuration(driving.getDuration());
        notificationDTO.setPrice(driving.getPrice());
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        List<String> intermediateStationDTO = new ArrayList<>();
        for (Address intermediateStation : driving.getRoute().getIntermediateStations()) {
            intermediateStationDTO.add(intermediateStation.getName());
        }
        notificationDTO.setIntermediateLocations(intermediateStationDTO);
        notificationDTO.setUserEmail(passengerEmail);
        notificationDTO.setNotificationStatus(NotificationStatus.NEW_DRIVING);
        this.simpMessagingTemplate.convertAndSend("/notification/newRide", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationRejectDriving(List<String> passengersToSendNotifications, String startLocation, String endLocation) {
        for (String passengerEmail : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("The ride was canceled by passenger");
            notificationDTO.setStartLocation(startLocation);
            notificationDTO.setEndLocation(endLocation);
            notificationDTO.setUserEmail(passengerEmail);
            notificationDTO.setNotificationStatus(NotificationStatus.REJECTED_DRIVING_PASSENGER);
            this.simpMessagingTemplate.convertAndSend("/notification/cancelRide", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationCancelDrivingFromDriverToAdmin(String adminEmail, Driving driving, String driverEmail, String reason) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(reason);
        notificationDTO.setDuration(driving.getDuration());
        notificationDTO.setPrice(driving.getPrice());
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        List<String> intermediateStationDTO = new ArrayList<>();
        for (Address intermediateStation : driving.getRoute().getIntermediateStations()) {
            if (intermediateStation != null){
                intermediateStationDTO.add(intermediateStation.getName());
            }
        }
        notificationDTO.setIntermediateLocations(intermediateStationDTO);
        notificationDTO.setUserEmail(adminEmail);
        notificationDTO.setDriverEmail(driverEmail);
        notificationDTO.setDrivingId(driving.getId());
        notificationDTO.setNotificationStatus(NotificationStatus.REJECTED_DRIVING_DRIVER);
        this.simpMessagingTemplate.convertAndSend("/notification/cancelRideDriver", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationForPaymentExpired(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment session has expired. Your current driving is canceled");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_EXPIRED);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentSessionExpired", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationForPaymentFailure(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage(ExceptionMessageConstants.PAYMENT_FAILURE);
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_FAILURE);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentFailure", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationForPaymentSuccess(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Payment success.");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_SUCCESS);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentSuccess", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationForNewDrivingToDriver(String driverEmail) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You have been assigned a new ride.");
        notificationDTO.setUserEmail(driverEmail);
        notificationDTO.setNotificationStatus(NotificationStatus.NEW_DRIVING_DRIVER);
        this.simpMessagingTemplate.convertAndSend("/notification/newRideDriver", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationForCanceledReservation(String userEmail, Driving driving) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage("You reservations is canceled");
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        notificationDTO.setUserEmail(userEmail);
        notificationDTO.setNotificationStatus(NotificationStatus.REJECTED_RESERVATION);
        this.simpMessagingTemplate.convertAndSend("/notification/cancelReservation", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationDriverArrivedAtStartLocation(Collection<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Your driver arrived at start location");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.DRIVER_ARRIVED_AT_START);
            this.simpMessagingTemplate.convertAndSend("/notification/driverArrivedStart", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void reportDriverNotification(Passenger initiator) {
        Driving driving = initiator.getCurrentDriving();
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(String.format("Passenger %s just reported driver %s", initiator.getEmail(), driving.getDriver().getEmail()));
        notificationDTO.setDuration(driving.getDuration());
        notificationDTO.setPrice(driving.getPrice());
        notificationDTO.setStartLocation(driving.getRoute().getStart().getName());
        notificationDTO.setEndLocation(driving.getRoute().getEnd().getName());
        List<String> intermediateStationDTO = new ArrayList<>();
        for (Address intermediateStation : driving.getRoute().getIntermediateStations()) {
            intermediateStationDTO.add(intermediateStation.getName());
        }
        notificationDTO.setIntermediateLocations(intermediateStationDTO);
        notificationDTO.setUserEmail(adminService.findAdmin().getEmail());
        notificationDTO.setNotificationStatus(NotificationStatus.REPORT_DRIVER);
        this.simpMessagingTemplate.convertAndSend("/notification/reportDriver", notificationDTO);
        createAndSaveNotification(notificationDTO);
    }

    @Override
    public void sendNotificationCancelDrivingFromDriverToPassengers(List<String> passengersToSendNotifications) {
        for (String passenger : passengersToSendNotifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Your driving is canceled by driver.");
            notificationDTO.setUserEmail(passenger);
            notificationDTO.setNotificationStatus(NotificationStatus.REGULAR_DRIVING_CANCELED_TO_PASSENGER);
            this.simpMessagingTemplate.convertAndSend("/notification/regularDrivingCanceledPassenger", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationReservationReminder(Integer startMinutes, List<User> userForNotification) {
        for (User u : userForNotification) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage(String.format("The expected reservation time is in %s minutes, if the driver and passengers are ready, it can start even earlier.", startMinutes));
            notificationDTO.setUserEmail(u.getEmail());
            notificationDTO.setNotificationStatus(NotificationStatus.RESERVATION_REMINDER);
            this.simpMessagingTemplate.convertAndSend("/notification/reservationReminder", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationForPaymentReservation(Driving d) {
        for (Passenger p : d.getAllPassengers()) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage("Your reservation starts in 15 minutes, please make the payment");
            notificationDTO.setDuration(d.getDuration());
            notificationDTO.setPrice(d.getPrice());
            notificationDTO.setStartLocation(d.getRoute().getStart().getName());
            notificationDTO.setEndLocation(d.getRoute().getEnd().getName());
            List<String> intermediateStationDTO = new ArrayList<>();
            for (Address intermediateStation : d.getRoute().getIntermediateStations()) {
                intermediateStationDTO.add(intermediateStation.getName());
            }
            notificationDTO.setIntermediateLocations(intermediateStationDTO);
            notificationDTO.setUserEmail(p.getEmail());
            notificationDTO.setDrivingId(d.getId());
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_RESERVATION);
            this.simpMessagingTemplate.convertAndSend("/notification/paymentReservation", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationForReservationDeleted(Driving d, String message) {
        List<User> userForNotification = new ArrayList<>(d.getAllPassengers());
        userForNotification.add(d.getDriver());
        for(User u : userForNotification){
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setMessage(message);
            notificationDTO.setDuration(d.getDuration());
            notificationDTO.setPrice(d.getPrice());
            notificationDTO.setStartLocation(d.getRoute().getStart().getName());
            notificationDTO.setEndLocation(d.getRoute().getEnd().getName());
            List<String> intermediateStationDTO = new ArrayList<>();
            for (Address intermediateStation : d.getRoute().getIntermediateStations()) {
                intermediateStationDTO.add(intermediateStation.getName());
            }
            notificationDTO.setIntermediateLocations(intermediateStationDTO);
            notificationDTO.setUserEmail(u.getEmail());
            notificationDTO.setNotificationStatus(NotificationStatus.PAYMENT_RESERVATION);
            this.simpMessagingTemplate.convertAndSend("/notification/reservationDeleted", notificationDTO);
            createAndSaveNotification(notificationDTO);
        }
    }

    @Override
    public void sendNotificationToAdminForDriverChangeData(UserDTO driverDTO) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEmail(adminService.findAdmin().getEmail());
        notificationDTO.setDriverData(driverDTO);
        notificationDTO.setNotificationStatus(NotificationStatus.DRIVER_CHANGE_DATA);
        notificationDTO.setMessage("Driver change profile data");
        Notification notification = createAndSaveNotification(notificationDTO);
        notificationDTO.setId(notification.getId());
        this.simpMessagingTemplate.convertAndSend("/notification/driverChangeData", notificationDTO);
    }

    @Override
    public void sendNotificationToAdminForCarChangeData(CarDTO carDTO) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEmail(adminService.findAdmin().getEmail());
        notificationDTO.setCarData(carDTO);
        notificationDTO.setNotificationStatus(NotificationStatus.CAR_CHANGE_DATA);
        notificationDTO.setMessage("Driver change car data");
        Notification notification = createAndSaveNotification(notificationDTO);
        notificationDTO.setId(notification.getId());
        this.simpMessagingTemplate.convertAndSend("/notification/carDataChange", notificationDTO);

    }

    @Override
    public void sendNotificationAdminResponseForChanges(String driverEmail, String response ) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEmail(driverEmail);
        notificationDTO.setMessage("Admin " + response + " your changes");
        notificationDTO.setNotificationStatus(NotificationStatus.ADMIN_RESPONSE);
        Notification notification = createAndSaveNotification(notificationDTO);
        notificationDTO.setId(notification.getId());
        this.simpMessagingTemplate.convertAndSend("/notification/admin-responses", notificationDTO);
    }

    @Override
    public List<NotificationDTO> findAllForLoggedUser() {
        User user = userService.getCurrentlyLoggedUser();
        return notificationRepository.findAllByUserEmail(user.getEmail())
                .stream().map(NotificationDTO::new)
                .sorted((n1, n2) -> n2.getCreationDate().compareTo(n1.getCreationDate())).collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> findAllForUserByEmail(String email) {
        return notificationRepository.findAllByUserEmail(email)
                .stream().map(NotificationDTO::new)
                .sorted((n1, n2) -> n2.getCreationDate().compareTo(n1.getCreationDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteNotification(Long id) {
        Notification notification = this.notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageConstants.NOTIFICATION_DOESNT_EXIST));
        notificationRepository.delete(notification);
    }

    private Notification createAndSaveNotification(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setPrice(notificationDTO.getPrice());
        notification.setReservationDate(notificationDTO.getReservationTime());
        notification.setMessage(notificationDTO.getMessage());
        notification.setDuration(notificationDTO.getDuration());
        notification.setStartLocation(notificationDTO.getStartLocation());
        notification.setEndLocation(notificationDTO.getEndLocation());
        notification.setNotificationStatus(notificationDTO.getNotificationStatus());
        notification.setUserEmail(notificationDTO.getUserEmail());
        notification.setCreationDate(LocalDateTime.now());
        notification.setDrivingId(notificationDTO.getDrivingId());
        notification.setDrivarStr(getDriverData(notificationDTO.getDriverData()));
        notification.setCarStr(getCarData(notificationDTO.getCarData()));
        return notificationRepository.save(notification);
    }

    private String getDriverData(UserDTO driverDTO){
        if (driverDTO == null) {
            return "";
        }
         return  "First name |" + driverDTO.getFirstName() + "| Last name: |" + driverDTO.getLastName() +
                "| Email: |" + driverDTO.getEmail() + "| Phone number: |" + driverDTO.getPhoneNumber();
    }

    private String getCarData(CarDTO carDTO){
        if (carDTO == null) {
            return "";
        }
        return  "Registration: |" + carDTO.getRegistration() + "| Model: |" + carDTO.getModel() +
                "| Type: |" + carDTO.getCarType() + "| max passengers |" + carDTO.getMaxPassengers()
                + "| accommodations: |" + carDTO.getAccommodations() + "| driver: |" + carDTO.getDriverEmail()
                + "| id |" + carDTO.getId();
    }
}