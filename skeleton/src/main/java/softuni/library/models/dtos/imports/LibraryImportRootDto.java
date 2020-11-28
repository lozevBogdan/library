package softuni.library.models.dtos.imports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "libraries")
@XmlAccessorType(XmlAccessType.FIELD)
public class LibraryImportRootDto {

    @XmlElement
    private List<LibraryImportDto> library;

    public LibraryImportRootDto() {
    }


    public List<LibraryImportDto> getLibrary() {
        return library;
    }

    public void setLibrary(List<LibraryImportDto> library) {
        this.library = library;
    }
}
