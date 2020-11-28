package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.imports.AuthorImportDto;
import softuni.library.models.entities.Author;
import softuni.library.repositories.AuthorRepository;
import softuni.library.services.AuthorService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.ValidatorUtilImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

private final String PATH_AUTHORS = "C:\\Users\\lozev\\Downloads\\Library_Skeleton\\skeleton\\src\\main\\resources\\files\\json\\authors.json";


private final AuthorRepository authorRepository;
private final ModelMapper modelMapper;
private final Gson gson;
private final ValidatorUtil validatorUtil;

@Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository,
                             ModelMapper modelMapper, Gson gson,
                             ValidatorUtil validatorUtil) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
}

    @Override
    public boolean areImported() {
        return this.authorRepository.count() > 0;
    }

    @Override
    public String readAuthorsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PATH_AUTHORS)));

    }

    @Override
    public String importAuthors() throws IOException {
        StringBuilder sb = new StringBuilder();


        AuthorImportDto[] authorImportDtos = this.gson.fromJson
                (this.readAuthorsFileContent(),
                        AuthorImportDto[].class);

        for (AuthorImportDto authorDto : authorImportDtos) {

          Optional<Author> byNames = this.authorRepository.findByFirstNameAndLastName
                  (authorDto.getFirstName(),authorDto.getLastName());

                  if (this.validatorUtil.isValid(authorDto) && byNames.isEmpty()){

                      Author author = this.modelMapper.map(authorDto, Author.class);

                      this.authorRepository.saveAndFlush(author);

                      sb.append(String.format("Successfully imported Author - %s – %s%n",
                              author.getFirstName() + " " + author.getLastName(),author.getBirthTown()
                              ));


                  }else {

                      sb.append("Invalid Author").append(System.lineSeparator());
                  }



        }



        return sb.toString();
    }
}
