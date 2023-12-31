package com.example.eventplannerapp.io;

import com.example.eventplannerapp.io.entity.EventEntity;
import com.example.eventplannerapp.io.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long> {
    Iterable<EventEntity> findAllByUserDetails(UserEntity userEntity);
    EventEntity findByEventId(String eventId);
}
