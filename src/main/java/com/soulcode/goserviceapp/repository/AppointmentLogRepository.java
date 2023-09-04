package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.AppointmentLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentLogRepository extends MongoRepository<AppointmentLog, String> {
}
