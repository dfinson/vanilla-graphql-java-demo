package dev.sanda.puregraphqljavademo.dao;

import dev.sanda.puregraphqljavademo.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
