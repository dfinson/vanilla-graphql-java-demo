package dev.sanda.puregraphqljavademo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(exclude = "author")
public class BlogPost {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Column(length = 20000)
    private String content;
    @ManyToOne
    @JsonIgnore
    private Author author;
}
