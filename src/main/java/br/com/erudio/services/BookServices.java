package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Book;
import br.com.erudio.repositories.BookRepository;


@Service
public class BookServices {
    
    private Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;

    public List<BookVO> findAll() {
        logger.info("Finding All Books");
        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
        books.stream().forEach(b ->
            b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel()));
        return books;
    }

    public BookVO findById(Long id) {
        logger.info("Finding a new Book");
        var entity = repository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("No records found for this ID!"));
        BookVO vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one Book");
        var entity = DozerMapper.parseObject(book, Book.class);
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();
        logger.info("Update one Book");
        var entity = repository.findById(book.getId()).orElseThrow(() ->
            new ResourceNotFoundException("No records found for this ID!"));
        
        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Delete one book");

        var entity = repository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("No records found for this ID!"));

        repository.delete(entity);
    }

}
