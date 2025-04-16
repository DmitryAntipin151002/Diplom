package UserService.config;

import UserService.model.ChatType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class ChatTypeConverter implements AttributeConverter<ChatType, String> {

    @Override
    public String convertToDatabaseColumn(ChatType attribute) {
        // Проверяем, если значение null, то возвращаем null в БД
        return attribute == null ? null : attribute.name();
    }

    @Override
    public ChatType convertToEntityAttribute(String dbData) {
        // Проверяем, если значение null, то возвращаем null в сущность
        if (dbData == null) {
            return null;
        }

        // Преобразуем строку в соответствующее значение перечисления
        try {
            return ChatType.valueOf(dbData.toUpperCase()); // Преобразуем строку в верхний регистр
        } catch (IllegalArgumentException e) {
            // Если строка не соответствует ни одному из значений перечисления, можно вернуть значение по умолчанию
            // или выбросить исключение, в зависимости от логики
            return ChatType.GROUP; // или выбросить исключение, если необходимо
        }
    }
}
