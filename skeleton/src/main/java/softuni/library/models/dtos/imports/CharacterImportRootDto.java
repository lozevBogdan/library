package softuni.library.models.dtos.imports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "characters")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterImportRootDto {

    @XmlElement(name = "character")
    private List<CharacterImportXmlDto> characterImportXmlDtos;

    public CharacterImportRootDto() {
    }


    public List<CharacterImportXmlDto> getCharacterImportXmlDtos() {
        return characterImportXmlDtos;
    }

    public void setCharacterImportXmlDtos(List<CharacterImportXmlDto> characterImportXmlDtos) {
        this.characterImportXmlDtos = characterImportXmlDtos;
    }
}
