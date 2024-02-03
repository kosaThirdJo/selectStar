package com.threestar.selectstar.repository;

import com.threestar.selectstar.entity.Portfolio;
import com.threestar.selectstar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    //Portfolio findByUser(User user);

    Optional<Portfolio> findByUser(User user);
}
