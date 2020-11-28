package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.imports.CharacterImportRootDto;
import softuni.library.models.dtos.imports.CharacterImportXmlDto;
import softuni.library.models.entities.Book;
import softuni.library.models.entities.Character;
import softuni.library.repositories.AuthorRepository;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.CharacterRepository;
import softuni.library.services.CharacterService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService {

    private final String PATH_CHARACTERS =
            "C:\\Users\\lozev\\Downloads\\Library_Skeleton\\skeleton\\src\\main\\resources\\files\\xml\\characters.xml";


    private final CharacterRepository characterRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final AuthorRepository authorRepository;
    private final XmlParser xmlParser;
    private final BookRepository bookRepository;

    public CharacterServiceImpl(CharacterRepository characterRepository, ModelMapper modelMapper, Gson gson,
                                ValidatorUtil validatorUtil, AuthorRepository authorRepository, XmlParser xmlParser, BookRepository bookRepository) {
        this.characterRepository = characterRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.authorRepository = authorRepository;
        this.xmlParser = xmlParser;
        this.bookRepository = bookRepository;
    }


    @Override
    public boolean areImported() {
        return this.characterRepository.count()>0;
    }

    @Override
    public String readCharactersFileContent() throws IOException {
        return Files.readString(Path.of(PATH_CHARACTERS));
    }

    @Override
    public String importCharacters() throws IOException, JAXBException {
        StringBuilder sb  = new StringBuilder();

        CharacterImportRootDto characterImportRootDto =
                this.xmlParser.parseXml(CharacterImportRootDto.class, PATH_CHARACTERS);


        for (CharacterImportXmlDto characterDto : characterImportRootDto.getCharacterImportXmlDtos()) {

            Optional<Book> bookById =
                    this.bookRepository.findById(characterDto.getBookImportCharacterDto().getId());


            if (this.validatorUtil.isValid(characterDto) && bookById.isPresent()){

                Character character = this.modelMapper.map(characterDto, Character.class);

                character.setBook(bookById.get());
                this.characterRepository.saveAndFlush(character);

                sb.append(String.format("Successfully imported Character - " +
                        "%s - age: %d%n",
                        character.getFirstName()+ " " + character.getMiddleName() +
                                " " + character.getLastName(),character.getAge()));

            }else {
                sb.append("Invalid Character").append(System.lineSeparator());
            }





        }

        return sb.toString();
    }

    @Override
    public String findCharactersInBookOrderedByLastNameDescendingThenByAge() {
        StringBuilder sb  = new StringBuilder();

        List<Character> characters = this.characterRepository.getCharacters();

        for (Character character : characters) {
            sb.append(String.format("Character name %s, age %d, in book %s%n",
                    character.getFirstName() + " "+ character.getMiddleName() + " " + character.getLastName(),
                    character.getAge(),character.getBook().getName()));

        }


        return sb.toString();
    }
}
