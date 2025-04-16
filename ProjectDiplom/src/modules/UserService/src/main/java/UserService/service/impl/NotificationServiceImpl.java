package UserService.service.impl;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EventPublisher eventPublisher;

    @Override
    public List<NotificationDto> getUserNotifications(UUID userId, boolean unreadOnly) {
        return notificationRepository.findByUserIdAndIsRead(userId, !unreadOnly)
                .stream()
                .map(notif -> {
                    NotificationDto dto = modelMapper.map(notif, NotificationDto.class);
                    dto.setRelatedEntityType(notif.getRelatedEntityType().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void createNotification(NotificationCreateDto createDto) {
        User user = userRepository.findById(createDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(createDto.getUserId()));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(createDto.getTitle());
        notification.setMessage(createDto.getMessage());
        notification.setType(createDto.getType());
        notification.setRelatedEntityType(
                RelatedEntityType.valueOf(createDto.getRelatedEntityType()));
        notification.setRelatedEntityId(createDto.getRelatedEntityId());

        Notification savedNotification = notificationRepository.save(notification);
        eventPublisher.publishEvent(new NotificationEvent(savedNotification));
    }
}