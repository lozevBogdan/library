package softuni.library.services.impl;

import com.google.gson.Gson;
import java.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.imports.LibraryImportDto;
import softuni.library.models.dtos.imports.LibraryImportRootDto;
import softuni.library.models.entities.Book;
import softuni.library.models.entities.Library;
import softuni.library.repositories.AuthorRepository;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.CharacterRepository;
import softuni.library.repositories.LibraryRepository;
import softuni.library.services.LibraryService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final String PATH_LIBRARY =
            "C:\\Users\\lozev\\Downloads\\Library_Skeleton\\skeleton\\src\\main\\resources\\files\\xml\\libraries.xml";


    private final CharacterRepository characterRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final AuthorRepository authorRepository;
    private final XmlParser xmlParser;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    public LibraryServiceImpl(CharacterRepository characterRepository, ModelMapper modelMapper, Gson gson, ValidatorUtil validatorUtil, AuthorRepository authorRepository, XmlParser xmlParser, BookRepository bookRepository, LibraryRepository libraryRepository) {
        this.characterRepository = characterRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.authorRepository = authorRepository;
        this.xmlParser = xmlParser;
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
    }


    @Override
    public boolean areImported() {

        return this.libraryRepository.count() > 0;
    }

    @Override
    public String readLibrariesFileContent() throws IOException {

        return Files.readString(Path.of(PATH_LIBRARY));
    }

    @Override
    public String importLibraries() throws JAXBException {

        StringBuilder sb = new StringBuilder();

        LibraryImportRootDto libraryImportRootDto = this.xmlParser.parseXml(LibraryImportRootDto.class, PATH_LIBRARY);


        for (LibraryImportDto libraryDto : libraryImportRootDto.getLibrary()) {


            Optional<Book> bookById =
                    this.bookRepository.findById(libraryDto.getBook().getId());

            if (this.validatorUtil.isValid(libraryDto) && bookById.isPresent()){

                Library library = this.modelMapper.map(libraryDto, Library.class);

                Set<Book> bookSet = new HashSet<>();
                bookSet.add(bookById.get());

                library.setBooks(bookSet);

                this.libraryRepository.saveAndFlush(library);

                sb.append(String.format("Successfully importedLibrary - " +
                        "%s – %s%n",library.getName(),library.getLocation()));

            }else {
                sb.append("Invalid Library").append(System.lineSeparator());
            }


        }


        return sb.toString();
    }
}
