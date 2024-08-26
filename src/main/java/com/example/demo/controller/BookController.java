package com.example.demo.controller;

import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService service;

    @QueryMapping
    public Book bookById(@Argument String id) {
        return service.getById(id);
    }

    @QueryMapping
    public List<Book> getBooks() {
        return service.getBooks();
    }

    @SchemaMapping
    public Author author(Book book) {
        return Author.getById(book.getAuthorId());
    }

    @MutationMapping
    public Book addBook (@Argument String author,
                         @Argument String name) {
        return service.add(name, author);
    }

    @SubscriptionMapping("newBookReady")
    public Publisher<Book> newBookReady() {
        return service.getFluxBooks();
        // A flux is the publisher of data

    }
}
