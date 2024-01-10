package com.threestar.selectstar.repository;

import java.util.List;
import java.util.Optional;

import com.threestar.selectstar.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {

	Optional<Object> findByName(String name);
	// 로그인 (닉네임, 탈퇴하지 않은 회원)
	User findByNameAndDeleted(String name, int deleted);
    Optional<Object> findByNickname(String nickname);
	// 로그인 확인 (아이디(name)와 비밀번호로 일치하는지 확인)
	Optional<User> findByNameAndPassword(String name, String password);
	// 회원 검색 (닉네임)
	List<User> findByNicknameLike(String nickname);

	User findByUserId(int id);

}