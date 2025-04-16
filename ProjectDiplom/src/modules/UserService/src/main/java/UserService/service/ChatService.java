package UserService.service;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с чатами и сообщениями
 */
public interface ChatService {

    /**
     * Создает новый чат
     * @param createDto DTO с данными для создания чата
     * @return DTO созданного чата
     * @throws UserNotFoundException если участник не найден
     */
    @Transactional
    ChatDto createChat(ChatCreateDto createDto) throws UserNotFoundException;

    /**
     * Получает сообщения чата
     * @param chatId ID чата
     * @param pageable параметры пагинации
     * @return список сообщений
     */
    List<MessageDto> getChatMessages(UUID chatId, Pageable pageable);

    /**
     * Отправляет сообщение в чат
     * @param chatId ID чата
     * @param senderId ID отправителя
     * @param sendDto DTO с данными сообщения
     * @return DTO отправленного сообщения
     * @throws ChatNotFoundException если чат не найден
     * @throws UserNotFoundException если отправитель не найден
     * @throws MessageNotFoundException если сообщение для ответа не найдено
     */
    @Transactional
    MessageDto sendMessage(UUID chatId, UUID senderId, MessageSendDto sendDto)
            throws ChatNotFoundException, UserNotFoundException, MessageNotFoundException;

    /**
     * Получает список чатов пользователя
     * @param userId ID пользователя
     * @return список чатов
     */
    List<ChatDto> getUserChats(UUID userId);

    /**
     * Добавляет участника в чат
     * @param chatId ID чата
     * @param userId ID пользователя
     * @return DTO обновленного чата
     * @throws ChatNotFoundException если чат не найден
     * @throws UserNotFoundException если пользователь не найден
     * @throws ChatAccessDeniedException если нет прав на управление чатом
     */
    @Transactional
    ChatDto addParticipant(UUID chatId, UUID userId)
            throws ChatNotFoundException, UserNotFoundException, ChatAccessDeniedException;

    /**
     * Помечает сообщения как прочитанные
     * @param chatId ID чата
     * @param userId ID пользователя
     * @throws ChatNotFoundException если чат не найден
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional
    void markMessagesAsRead(UUID chatId, UUID userId)
            throws ChatNotFoundException, UserNotFoundException;
}