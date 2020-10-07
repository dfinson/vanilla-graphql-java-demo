package dev.sanda.puregraphqljavademo.service;

import dev.sanda.puregraphqljavademo.dao.AuthorRepository;
import dev.sanda.puregraphqljavademo.model.Author;
import dev.sanda.puregraphqljavademo.model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author createAuthor(Author input){
        return authorRepository.save(input);
    }

    public Author getAuthorById(Long id){
        return authorRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cannot find Author with id #" + id)
                );
    }

    public Set<BlogPost> getBlogPostsOfAuthor(Author author) {
        return authorRepository
                .findById(author.getId())
                .orElseThrow(() -> new RuntimeException("Cannot find Author with id #" + author.getId()))
                .getBlogPosts();
    }
}
