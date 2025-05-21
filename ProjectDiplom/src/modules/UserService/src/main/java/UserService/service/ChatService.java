package UserService.service;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    /**
     * Создает новый чат с указанными параметрами
     * @param request DTO с данными для создания чата
     * @return DTO созданного чата
     */
    ChatDto createChat(CreateChatRequest request);

    /**
     * Получает список чатов пользователя
     * @param userId идентификатор пользователя
     * @return список DTO чатов
     */
    List<ChatDto> getUserChats(UUID userId);

    /**
     * Получает информацию о чате
     * @param chatId идентификатор чата
     * @param userId идентификатор пользователя (для проверки доступа)
     * @return DTO чата
     * @throws ChatNotFoundException если чат не найден
     * @throws AccessDeniedException если пользователь не участник чата
     */
    ChatDto getChatInfo(UUID chatId, UUID userId) throws ChatNotFoundException;

    /**
     * Добавляет участников в чат
     * @param chatId идентификатор чата
     * @param participantIds список идентификаторов пользователей
     * @param requesterId идентификатор пользователя, инициирующего добавление
     * @throws ChatNotFoundException если чат не найден
     * @throws AccessDeniedException если пользователь не имеет прав на добавление
     */
    void addParticipants(UUID chatId, List<UUID> participantIds, UUID requesterId)
            throws ChatNotFoundException;

    /**
     * Удаляет участника из чата
     * @param chatId идентификатор чата
     * @param participantId идентификатор удаляемого пользователя
     * @param requesterId идентификатор пользователя, инициирующего удаление
     * @throws ChatNotFoundException если чат не найден
     * @throws AccessDeniedException если пользователь не имеет прав на удаление
     */
    void removeParticipant(UUID chatId, UUID participantId, UUID requesterId)
            throws ChatNotFoundException;

    ChatDto createEventChat(UUID eventId, String chatName, UUID creatorId);
}