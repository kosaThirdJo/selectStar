package com.threestar.selectstar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String nickname;

    @NotNull
    private String location1;

    private String location2;

    @CreationTimestamp
    private java.sql.Date joinDate;

    private byte[] profilePhoto;
    private String aboutMe;
    private String profileContent;
    private String interestLanguage;
    private String interestFramework;
    private String interestJob;

    @ColumnDefault(value = "'USER'")
    @Column(nullable = false)
    private String role;

    @ColumnDefault(value = "0")
    private int userStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(nickname, user.nickname) && Objects.equals(location1, user.location1) && Objects.equals(location2, user.location2) && Arrays.equals(profilePhoto, user.profilePhoto) && Objects.equals(aboutMe, user.aboutMe) && Objects.equals(profileContent, user.profileContent) && Objects.equals(interestLanguage, user.interestLanguage) && Objects.equals(interestFramework, user.interestFramework) && Objects.equals(interestJob, user.interestJob);
    }
    @Override
    public int hashCode() {
        int result = Objects.hash(userId, name, password, email, nickname, location1, location2, joinDate, aboutMe, profileContent, interestLanguage, interestFramework, interestJob);
        result = 31 * result + Arrays.hashCode(profilePhoto);
        return result;
    }

    @Builder
    public User(String name, String password, String email, String nickname, String location1, String location2, Date joinDate, byte[] profilePhoto, String aboutMe, String profileContent, String interestLanguage, String interestFramework, String interestJob) {
        //this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.location1 = location1;
        this.location2 = location2;
        this.joinDate = joinDate;
        this.profilePhoto = profilePhoto;
        this.aboutMe = aboutMe;
        this.profileContent = profileContent;
        this.interestLanguage = interestLanguage;
        this.interestFramework = interestFramework;
        this.interestJob = interestJob;
    }

    public void updatePassword(String password){
        this.password = password;
    }
    public void updateUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
