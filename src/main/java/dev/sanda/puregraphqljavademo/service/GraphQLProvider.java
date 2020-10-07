package dev.sanda.puregraphqljavademo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sanda.puregraphqljavademo.model.Author;
import dev.sanda.puregraphqljavademo.model.BlogPost;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Service
public class GraphQLProvider {

    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        byte[] rawSchema = new ClassPathResource("schema.graphqls")
                .getInputStream()
                .readAllBytes();
        String sdl = new String(rawSchema);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        Map<String, DataFetcher> dataFetchers = dataFetchers();
        return RuntimeWiring.newRuntimeWiring()
                .scalar(Scalars.GraphQLLong)
                .type(newTypeWiring("Query")
                        .dataFetcher("authorById", dataFetchers.get("authorById")))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("createAuthor", dataFetchers.get("createAuthor"))
                        .dataFetcher("createBlogPost", dataFetchers.get("createBlogPost")))
                .type(newTypeWiring("Author")
                        .dataFetcher("blogPosts", dataFetchers.get("blogPostsOfAuthor")))
                .type(newTypeWiring("BlogPost")
                        .dataFetcher("author", dataFetchers.get("authorOfBlogPost")))
                .build();
    }

    @Autowired
    private AuthorService authorService;
    @Autowired
    private BlogPostService blogPostService;

    public Map<String, DataFetcher> dataFetchers(){
        ObjectMapper mapper = new ObjectMapper();
        return new HashMap<>() {{
            put("authorById",
                    dataFetchingEnvironment -> {
                        Long id = dataFetchingEnvironment.getArgument("id");
                        return authorService.getAuthorById(id);
                    });
            put("createAuthor",
                    dataFetchingEnvironment -> {
                        Author input = mapper.convertValue(dataFetchingEnvironment.getArgument("input"), Author.class);
                        return authorService.createAuthor(input);
                    });
            put("createBlogPost",
                    dataFetchingEnvironment -> {
                        Long authorId = dataFetchingEnvironment.getArgument("authorId");
                        BlogPost input = mapper.convertValue(dataFetchingEnvironment.getArgument("input"), BlogPost.class);
                        return blogPostService.createBlogPost(authorId, input);
                    });
            put("blogPostsOfAuthor",
                    dataFetchingEnvironment -> {
                        Author author = mapper.convertValue(dataFetchingEnvironment.getSource(), Author.class);
                        return authorService.getBlogPostsOfAuthor(author);
                    });
            put("authorOfBlogPost",
                    dataFetchingEnvironment -> {
                        Object source = dataFetchingEnvironment.getSource();
                        return blogPostService.getAuthorOfBlogPost((BlogPost) source);
                    });
        }};
    }
}
