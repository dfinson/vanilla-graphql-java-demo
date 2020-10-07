package dev.sanda.puregraphqljavademo.controller;

import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiEndpointController {

    @Autowired
    private GraphQL graphQL;

    @PostMapping("/graphql")
    public Map<String, Object> endpoint(@RequestBody Map<String, Object> graphQLQuery){
        return graphQL
                .execute((String) graphQLQuery.get("query"))
                .toSpecification();
    }
}
