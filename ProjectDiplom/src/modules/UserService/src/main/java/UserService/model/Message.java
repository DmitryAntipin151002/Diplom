package UserService.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime sentAt;

    private boolean isRead;
    private LocalDateTime readAt;

    @ManyToOne
    @JoinColumn(name = "reply_to_id")
    private Message replyTo;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT; // NEW: статус сообщения

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<MessageAttachment> attachments; // NEW: вложения

    public enum MessageStatus {
        SENT, DELIVERED, READ, EDITED, DELETED
    }
}