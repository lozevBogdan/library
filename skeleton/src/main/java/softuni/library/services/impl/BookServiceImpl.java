package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.imports.BooksImportDto;
import softuni.library.models.entities.Author;
import softuni.library.models.entities.Book;
import softuni.library.repositories.AuthorRepository;
import softuni.library.repositories.BookRepository;
import softuni.library.services.BookService;
import softuni.library.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final String PATH_BOOKS = "C:\\Users\\lozev\\Downloads\\Library_Skeleton\\skeleton\\src\\main\\resources\\files\\json\\books.json";


    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper,
                           Gson gson, ValidatorUtil validatorUtil, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean areImported() {
        return this.bookRepository.count() > 0;
    }

    @Override
    public String readBooksFileContent() throws IOException {
        return Files.readString(Path.of(PATH_BOOKS));
    }

    @Override
    public String importBooks() throws IOException {
        StringBuilder sb = new StringBuilder();

        BooksImportDto[] booksDtos =
                this.gson.fromJson(this.readBooksFileContent(), BooksImportDto[].class);



        for (BooksImportDto bookDto : booksDtos) {

            Optional<Book> bookByDescription =
                    this.bookRepository.findBookByDescription(bookDto.getDescription());

            Optional<Author> authorById = this.authorRepository.findById(bookDto.getAuthor());


            if (this.validatorUtil.isValid(bookDto) &&
                    bookByDescription.isEmpty() &&
                    authorById.isPresent() ){

                Book book = this.modelMapper.map(bookDto, Book.class);
                book.setAuthor(authorById.get());

               this.bookRepository.saveAndFlush(book);

               sb.append(String.format("Successfully imported Book - " +
                       "%s written in %s%n",
                       book.getName(),book.getWritten()));



            }else {
                sb.append("Invalid Book").append(System.lineSeparator());
            }


        }



        return sb.toString();
    }
}
