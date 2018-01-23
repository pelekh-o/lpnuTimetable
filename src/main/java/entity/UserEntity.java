package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "timetable")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -2557237556928683019L;

    private Integer id;
    private Integer userId;
    private String name;
    private String username;
    private String instituteTitle;
    private String groupTitle;
    private Byte isRemembered;
    private Date regDate;
    private Set<ChatEntity> chatEntities = new HashSet<>(0);

    public UserEntity() {
    }

    public UserEntity(Integer userId, String name, String username,
                      String instituteTitle, String groupTitle, Byte isRemembered, Date regDate) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.instituteTitle = instituteTitle;
        this.groupTitle = groupTitle;
        this.isRemembered = isRemembered;
        this.regDate = regDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "userID")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "instituteTitle")
    public String getInstituteTitle() {
        return instituteTitle;
    }

    public void setInstituteTitle(String instituteTitle) {
        this.instituteTitle = instituteTitle.toUpperCase();
    }

    @Basic
    @Column(name = "groupTitle")
    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle.toUpperCase();
    }

    @Basic
    @Column(name = "isRemembered")
    public Byte getIsRemembered() {
        return isRemembered;
    }

    public void setIsRemembered(Byte isRemembered) {
        this.isRemembered = isRemembered;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regDate")
    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<ChatEntity> getChatEntities() {
        return chatEntities;
    }

    public void setChatEntities(Set<ChatEntity> chatEntities) {
        this.chatEntities = chatEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (userId != that.userId) return false;
        if (isRemembered != that.isRemembered) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (instituteTitle != null ? !instituteTitle.equals(that.instituteTitle) : that.instituteTitle != null)
            return false;
        if (groupTitle != null ? !groupTitle.equals(that.groupTitle) : that.groupTitle != null) return false;
        if (regDate != null ? !regDate.equals(that.regDate) : that.regDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (instituteTitle != null ? instituteTitle.hashCode() : 0);
        result = 31 * result + (groupTitle != null ? groupTitle.hashCode() : 0);
        result = 31 * result + (int) isRemembered;
        result = 31 * result + (regDate != null ? regDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "id: " + id + ", userId: " + userId + ", name: " + name + ", username: " + username
                + ", instituteTitle: " + instituteTitle + ", groupTitle: " + groupTitle
                + ", isRemembered: " + isRemembered + ", regDate: " + regDate;
    }
}
