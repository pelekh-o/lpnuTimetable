package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "message", schema = "timetable")
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 2013373998701812023L;

    private Integer id;
    private Integer messageId;
    private String messageText;
    private Set<AnswerEntity> answerEntities = new HashSet<AnswerEntity>(0);
    private ChatEntity chat;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "messageID")
    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    @Basic
    @Column(name = "messageText")
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message")
    public Set<AnswerEntity> getAnswerEntities() {
        return answerEntities;
    }

    public void setAnswerEntities(Set<AnswerEntity> answerEntities) {
        this.answerEntities = answerEntities;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatID", nullable = true)
    public ChatEntity getChat() {
        return chat;
    }

    public void setChat(ChatEntity chat) {
        this.chat = chat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (messageId != that.messageId) return false;
        if (messageText != null ? !messageText.equals(that.messageText) : that.messageText != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (messageText != null ? messageText.hashCode() : 0);
        return result;
    }
}
