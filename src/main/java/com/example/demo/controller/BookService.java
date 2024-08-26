package com.example.demo.controller;

import com.example.demo.entity.Book;
import lombok.Getter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Getter
public class BookService {

    private List<Book> books;

    private final Sinks.Many<Book> sink = Sinks.many().multicast().directBestEffort();

    BookService() {
        books = new ArrayList<>();

        List<Book> books1 = Arrays.asList(
                new Book("book-1", "Harry Potter and the Philosopher's Stone", 223, "author-1"),
                new Book("book-2", "Moby Dick", 635, "author-2"),
                new Book("book-3", "Interview with the vampire", 371, "author-3")
        );

        books.addAll(books1);

    }


    public Book getById(String id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
    }

    public Book add(String name, String auth) {
        int nextId = books.size() + 1;
        Book book = new Book("new-"+nextId, name, 10, auth);
        books.add(book);
        this.sink.tryEmitNext(book);
        return book;
    }

    public Flux<Book> getFluxBooks() {
        return Flux.concat(Flux.fromIterable(this.books), sink.asFlux());
    }
}
