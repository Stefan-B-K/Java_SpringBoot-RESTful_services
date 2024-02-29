package com.istef.rest_webservices.dao;

import com.istef.rest_webservices.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
