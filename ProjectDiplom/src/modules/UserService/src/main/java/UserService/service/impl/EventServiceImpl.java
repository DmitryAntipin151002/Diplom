package UserService.service.impl;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.EventService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EventStatusRepository eventStatusRepository;

    @PostConstruct
    public void init() {
        initStatuses();
    }

    private void initStatuses() {
        createStatusIfNotExists("PLANNED", "Запланировано");
        createStatusIfNotExists("ONGOING", "В процессе");
        createStatusIfNotExists("COMPLETED", "Завершено");
        createStatusIfNotExists("CANCELLED", "Отменено");
    }

    private void createStatusIfNotExists(String code, String name) {
        if (!eventStatusRepository.existsByCode(code)) {
            EventStatus status = new EventStatus();
            status.setCode(code);
            status.setName(name);
            eventStatusRepository.save(status);
        }
    }

    @Override
    @Transactional
    public EventDto createEvent(UUID organizerId, EventCreateDto createDto) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(organizerId));

        Event event = modelMapper.map(createDto, Event.class);
        event.setOrganizer(organizer);

        // Устанавливаем статус (по умолчанию PLANNED)
        String statusCode = createDto.getStatusCode() != null ?
                createDto.getStatusCode() : "PLANNED";

        EventStatus status = eventStatusRepository.findByCode(statusCode)
                .orElseThrow(() -> new IllegalStateException("Invalid event status: " + statusCode));

        event.setStatus(status);

        Event savedEvent = eventRepository.save(event);
        return mapToDto(savedEvent);
    }

    private EventDto mapToDto(Event event) {
        EventDto dto = modelMapper.map(event, EventDto.class);
        dto.setOrganizerId(event.getOrganizer().getId());
        dto.setOrganizerName(event.getOrganizer().getEmail());
        dto.setStatusCode(event.getStatus().getCode());
        return dto;
    }

    @Override
    public EventDto getEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setOrganizerId(event.getOrganizer().getId());
        eventDto.setOrganizerName(event.getOrganizer().getEmail());

        return eventDto;
    }

    @Override
    public List<EventDto> getEventsByOrganizer(UUID organizerId) {
        if (!userRepository.existsById(organizerId)) {
            throw new UserNotFoundException(organizerId);
        }

        return eventRepository.findByOrganizerId(organizerId).stream()
                .map(event -> {
                    EventDto dto = modelMapper.map(event, EventDto.class);
                    dto.setOrganizerId(event.getOrganizer().getId());
                    dto.setOrganizerName(event.getOrganizer().getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDto> getUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByStartTimeAfterOrderByStartTimeAsc(now).stream()
                .map(event -> {
                    EventDto dto = modelMapper.map(event, EventDto.class);
                    dto.setOrganizerId(event.getOrganizer().getId());
                    dto.setOrganizerName(event.getOrganizer().getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto updateEventStatus(UUID eventId, EventStatus status) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        event.setStatus(status);
        Event updatedEvent = eventRepository.save(event);

        EventDto eventDto = modelMapper.map(updatedEvent, EventDto.class);
        eventDto.setOrganizerId(event.getOrganizer().getId());
        eventDto.setOrganizerName(event.getOrganizer().getEmail());

        return eventDto;
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(eventId);
        }
        eventRepository.deleteById(eventId);
    }
}