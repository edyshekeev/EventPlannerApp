package com.example.eventplannerapp.service.impl;

import com.example.eventplannerapp.exceptions.UserServiceExceptions;
import com.example.eventplannerapp.io.EventRepository;
import com.example.eventplannerapp.io.UserRepository;
import com.example.eventplannerapp.io.entity.EventEntity;
import com.example.eventplannerapp.io.entity.UserEntity;
import com.example.eventplannerapp.service.EventService;
import com.example.eventplannerapp.shared.Utils;
import com.example.eventplannerapp.shared.dto.EventDto;
import com.example.eventplannerapp.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    Utils utils;
    @Override
    public EventDto createEvent(EventDto eventDto) {
        ModelMapper modelMapper = new ModelMapper();
        EventEntity eventEntity = modelMapper.map(eventDto, EventEntity.class);

        String eventId = utils.generateEventId(30);
        eventEntity.setEventId(eventId);

        EventEntity storedEvent = eventRepository.save(eventEntity);

        EventDto returnValue = modelMapper.map(storedEvent, EventDto.class);

        return returnValue;
    }

    public EventDto getEvent(String eventId) {
        EventDto returnValue = null;
        EventEntity eventEntity = eventRepository.findByEventId(eventId);

        if (eventEntity != null) {
            returnValue = new ModelMapper().map(eventEntity, EventDto.class);
        }
        return returnValue;
    }

    public EventDto updateEvent(String eventId, EventDto eventDto) {
        EventDto returnValue = new EventDto();
        EventEntity eventEntity = eventRepository.findByEventId(eventId);
        if (eventEntity == null) {
            throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        eventEntity.setTitle(eventDto.getTitle());
        eventEntity.setDescription(eventDto.getDescription());
        eventEntity.setDate(eventDto.getDate());
        eventEntity.setDuration(eventDto.getDuration());
        EventEntity updatedEntity = eventRepository.save(eventEntity);
        BeanUtils.copyProperties(updatedEntity, returnValue);

        return returnValue;
    }

    public void deleteEvent(String eventId) {
        EventEntity eventEntity = eventRepository.findByEventId(eventId);
        if (eventEntity == null) {
            throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        eventRepository.delete(eventEntity);
    }

    public List<EventDto> getEvents(String userId) {
        ModelMapper modelMapper = new ModelMapper();
        List<EventDto> returnValue = new ArrayList<>();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return returnValue;
        }

        Iterable<EventEntity> events = eventRepository.findAllByUserDetails(userEntity);
        for (EventEntity event : events) {
            returnValue.add(modelMapper.map(event, EventDto.class));
        }

        return returnValue;
    }
}
