package com.code.store.reviews;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface reviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByCompanyId(Long companyId);
}

