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
    private final EventParticipantRepository eventParticipantRepository;
    private final UserProfileRepository profileRepository;
    private final ChatRepository chatRepository;
    private final UserProfileRepository userProfileRepository;

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

    @Override
    @Transactional
    public EventParticipantDto addParticipant(UUID eventId, UUID userId, UUID organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new AccessDeniedException("Only organizer can add participants");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (eventParticipantRepository.existsByEventAndUser(event, user)) {
            throw new IllegalArgumentException("User already in event");
        }

        EventParticipant participant = new EventParticipant();
        participant.setEvent(event);
        participant.setUser(user);
        participant.setStatus(EventParticipant.ParticipantStatus.PENDING);
        participant.setRole(EventParticipant.ParticipantRole.PARTICIPANT);
        participant.setJoinedAt(LocalDateTime.now());

        EventParticipant saved = eventParticipantRepository.save(participant);
        return modelMapper.map(saved, EventParticipantDto.class);
    }

    @Override
    @Transactional
    public void removeParticipant(UUID eventId, UUID participantId, UUID requesterId) {
        EventParticipant participant = eventParticipantRepository.findById(participantId)
                .orElseThrow(() -> new EventNotFoundException(participantId));

        if (!participant.getEvent().getOrganizer().getId().equals(requesterId) &&
                !participant.getUser().getId().equals(requesterId)) {
            throw new AccessDeniedException("Not enough rights to remove participant");
        }

        eventParticipantRepository.delete(participant);
    }

    @Override
    public List<EventParticipantDto> getParticipants(UUID eventId) {
        return eventParticipantRepository.findByEventIdWithUser(eventId)
                .stream()
                .map(this::mapToParticipantDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfileDto> searchPotentialParticipants(UUID eventId, String query) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        List<UserProfile> profiles = profileRepository.searchProfiles(query, 10);

        return profiles.stream()
                .filter(profile -> !eventParticipantRepository.existsByEventAndUser(
                        event,
                        profile.getUser()
                ))
                .map(profile -> modelMapper.map(profile, ProfileDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public ProfileDto getParticipantInfo(UUID participantId) {
        EventParticipant participant = eventParticipantRepository.findById(participantId)
                .orElseThrow(() -> new EventNotFoundException(participantId));
        User user = participant.getUser();
        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EventNotFoundException(user.getId()));

        return modelMapper.map(profile, ProfileDto.class);
    }


    @Override
    @Transactional
    public ChatDto createEventChat(UUID eventId, String chatName, UUID creatorId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (!event.getOrganizer().getId().equals(creatorId)) {
            throw new AccessDeniedException("Only organizer can create chat");
        }

        Chat chat = new Chat();
        chat.setName(chatName);
        chat.setEvent(event);
        chat.setCreatorId(event.getOrganizer().getId());
        chat.setCreatedAt(LocalDateTime.now());

        Chat savedChat = chatRepository.save(chat);
        return modelMapper.map(savedChat, ChatDto.class);
    }

    @Override
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private EventParticipantDto mapToParticipantDto(EventParticipant participant) {
        EventParticipantDto dto = new EventParticipantDto();
        dto.setId(participant.getId());

        User user = participant.getUser();
        dto.setUserId(user.getId());

        UserProfile profile = user.getProfile();
        if(profile != null) {
            dto.setUserName(profile.getFirstName() + " " + profile.getLastName());
            dto.setUserAvatar(profile.getAvatarUrl());
            dto.setCity(profile.getLocation());
        } else {
            dto.setUserName(user.getEmail());
            dto.setUserAvatar("/default-avatar.png");
        }

        dto.setStatus(participant.getStatus());
        dto.setRole(participant.getRole());
        dto.setJoinedAt(participant.getJoinedAt());

        return dto;
    }
}