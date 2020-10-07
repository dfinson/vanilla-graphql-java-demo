package dev.sanda.puregraphqljavademo.dao;

import dev.sanda.puregraphqljavademo.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
