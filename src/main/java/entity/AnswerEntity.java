package entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "answer", schema = "timetable")
public class AnswerEntity implements Serializable {
    private static final long serialVersionUID = 8259448917848646801L;

    private Long answerId;
    private String answerText;
    private MessageEntity message;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerID", unique = true, nullable = false)
    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    @Basic
    @Column(name = "answerText", nullable = false)
    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageID", nullable = true)
    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnswerEntity that = (AnswerEntity) o;

        if (answerId != that.answerId) return false;
        if (answerText != null ? !answerText.equals(that.answerText) : that.answerText != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Math.toIntExact(answerId);
        result = 31 * result + (answerText != null ? answerText.hashCode() : 0);
        return result;
    }
}
