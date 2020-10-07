package dev.sanda.puregraphqljavademo.service;

import dev.sanda.puregraphqljavademo.dao.AuthorRepository;
import dev.sanda.puregraphqljavademo.dao.BlogPostRepository;
import dev.sanda.puregraphqljavademo.model.Author;
import dev.sanda.puregraphqljavademo.model.BlogPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public BlogPost createBlogPost(Long authorId, BlogPost input){
        Author author = authorRepository
                .findById(authorId)
                .orElseThrow(() ->
                        new RuntimeException("Cannot find Author with id #" + authorId)
                );
        BlogPost savedInstance = blogPostRepository.save(input);
        savedInstance.setAuthor(author);
        author.getBlogPosts().add(input);
        return savedInstance;
    }

    public Author getAuthorOfBlogPost(BlogPost blogPost){
        return blogPostRepository
                .findById(blogPost.getId())
                .orElseThrow(() -> new RuntimeException("Cannot find BlogPost with id #" + blogPost.getId()))
                .getAuthor();
    }
}
