package com.izzydrive.backend.jobs;

import com.izzydrive.backend.model.Driving;
import com.izzydrive.backend.service.driving.DrivingService;
import com.izzydrive.backend.service.driving.rejection.DrivingRejectionService;
import com.izzydrive.backend.service.driving.validation.DrivingValidationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PaymentSessionExpiredTask {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSessionExpiredTask.class);

    private final DrivingService drivingService;

    private final DrivingValidationService drivingValidationService;

    private final DrivingRejectionService drivingRejectionService;

    @Scheduled(cron = "${payment-session-expired-job.cron}")
    public void cancelDrivingForExpiredPaymentSession() {
        LOG.info("> job started");
        List<Driving> drivings = drivingService.getAllDrivingsInStatusPayment();
        for (Driving d : drivings) {
            if (drivingValidationService.drivingExpiredForPayment(d) && !d.isLocked()) {
                try {
                    drivingRejectionService.removeDrivingPaymentSessionExpired(d.getId());
                    LOG.info(String.format("> driving with id:%d successfully removed", d.getId()));
                } catch (OptimisticLockingFailureException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
    }
}
