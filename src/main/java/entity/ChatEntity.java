package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat", schema = "timetable")
public class ChatEntity implements Serializable {
    private static final long serialVersionUID = -3687689973973174049L;

    private Integer id;
    private Long chatId;
    private Byte isGroupChat;
    private Byte isUserChat;
    private Set<MessageEntity> messageEntities = new HashSet<>(0);
    private UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "chatID")
    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Basic
    @Column(name = "isGroupChat")
    public Byte getIsGroupChat() {
        return isGroupChat;
    }

    public void setIsGroupChat(Byte isGroupChat) {
        this.isGroupChat = isGroupChat;
    }

    @Basic
    @Column(name = "isUserChat")
    public Byte getIsUserChat() {
        return isUserChat;
    }

    public void setIsUserChat(Byte isUserChat) {
        this.isUserChat = isUserChat;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chat")
    public Set<MessageEntity> getMessageEntities() {
        return messageEntities;
    }

    public void setMessageEntities(Set<MessageEntity> messageEntities) {
        this.messageEntities = messageEntities;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = true)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatEntity that = (ChatEntity) o;

        if (chatId != that.chatId) return false;
        if (isGroupChat != that.isGroupChat) return false;
        if (isUserChat != that.isUserChat) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) isGroupChat;
        result = 31 * result + (int) isUserChat;
        return result;
    }
}
