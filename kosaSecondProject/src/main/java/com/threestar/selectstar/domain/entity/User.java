package com.threestar.selectstar.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users") //h2는 user가 예약어
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String name;
    private String password;
    private String email;
    private String nickname;
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
    private int deleted;
    private String role;

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

    public List<String> getRoleList(){
        if(this.role.length() > 0){
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }
}
