package com.example.eventplannerapp.ui.controller;

import com.example.eventplannerapp.exceptions.UserServiceExceptions;
import com.example.eventplannerapp.io.entity.UserEntity;
import com.example.eventplannerapp.service.EventService;
import com.example.eventplannerapp.service.UserService;
import com.example.eventplannerapp.shared.dto.EventDto;
import com.example.eventplannerapp.shared.dto.UserDto;
import com.example.eventplannerapp.ui.model.request.EventDetailsRequestModel;
import com.example.eventplannerapp.ui.model.request.UserDetailsRequestModel;
import com.example.eventplannerapp.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;

    @GetMapping(path="/{id}")
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnValue;

        if (userDetails.getUsername().isEmpty() || userDetails.getEmail().isEmpty() ||
                userDetails.getPassword().isEmpty()) {
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path="/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        userService.deleteUser(id);
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    @GetMapping(path = "/{userId}/events", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CollectionModel<EventsRest> getUserEvents(@PathVariable String userId) {
        List<EventsRest> returnValue = new ArrayList<>();
        List<EventDto> eventDtos = eventService.getEvents(userId);

        if (eventDtos != null && !eventDtos.isEmpty()) {
            Type listType = new TypeToken<List<EventsRest>>() {}.getType();
            returnValue = new ModelMapper().map(eventDtos, listType);

            for (EventsRest event : returnValue) {
                Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUserEvent(userId, event.getEventId()))
                        .withSelfRel();
                event.add(selfLink);
            }
        }

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserEvents(userId))
                .withSelfRel();

        return CollectionModel.of(returnValue).add(userLink).add(selfLink);
    }

    @GetMapping(path = "/{userId}/events/{eventId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EntityModel<EventsRest> getUserEvent(@PathVariable String userId, @PathVariable String eventId) {
        EventDto eventDto = eventService.getEvent(eventId);

        EventsRest returnValue = new ModelMapper().map(eventDto, EventsRest.class);

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        Link userEventsLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserEvents(userId))
                .withRel("events");

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserEvent(userId, eventId))
                .withSelfRel();

        return EntityModel.of(returnValue, Arrays.asList(userLink, userEventsLink, selfLink));
    }

    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token", defaultValue = "") String token) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified == true) {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    @PostMapping(path = "/{userId}/events")
    public EventsRest createEvent(String userId, @RequestBody EventDetailsRequestModel eventDetails) {
        EventsRest returnValue;

        if (eventDetails.getTitle().isEmpty() || eventDetails.getDescription().isEmpty()) {
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        EventDto eventDto = modelMapper.map(eventDetails, EventDto.class);
        UserDto author = userService.getUserByUserId(userId);
        eventDto.setAuthor(author);

        List<EventDto> eventDtos = eventService.getEvents(userId);
        eventDtos.add(eventDto);

        UserDto userDto = new UserDto();
        userDto.setEvents(eventDtos);
        userService.updateUser(userId, userDto);

        EventDto createdEvent = eventService.createEvent(eventDto);
        returnValue = modelMapper.map(createdEvent, EventsRest.class);

        return returnValue;
    }

    @PutMapping(path="/{userId}/events/{eventId}")
    public EventsRest updateEvent(@PathVariable String userId, @PathVariable String eventId, @RequestBody EventDetailsRequestModel eventDetails) {
        EventsRest returnValue = new EventsRest();

        EventDto eventDto = new EventDto();
        BeanUtils.copyProperties(eventDetails, eventDto);

        EventDto updatedEvent = eventService.updateEvent(eventId, eventDto);
        BeanUtils.copyProperties(updatedEvent, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{userId}/events/{eventId}")
    public OperationStatusModel deleteEvent(@PathVariable String userId, @PathVariable String eventId) {
        OperationStatusModel returnValue = new OperationStatusModel();
        EventDto eventDto = eventService.getEvent(eventId);

        List<EventDto> eventDtos = eventService.getEvents(userId);
        eventDtos.remove(eventDto);

        UserDto userDto = new UserDto();
        userDto.setEvents(eventDtos);
        userService.updateUser(userId, userDto);

        eventService.deleteEvent(eventId);
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }
}
