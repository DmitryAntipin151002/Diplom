package UserService.service;



import UserService.dto.NotificationCreateDto;
import UserService.dto.NotificationDto;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> getUserNotifications(UUID userId, boolean unreadOnly);

    @Transactional
    void markAsRead(UUID notificationId);

    @Transactional
    void createNotification(NotificationCreateDto createDto);
}