package softuni.library.models.entities;

import com.fasterxml.jackson.databind.ser.Serializers;
import java.util.Set;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    private String name;
    private int edition;
    private LocalDate written;
    private String description;
    private Author author;
    private Set<Character> characters;
    private Set<Library> libraries;


    public Book() {
    }


    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column
    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }
    @Column
    public LocalDate getWritten() {
        return written;
    }

    public void setWritten(LocalDate written) {
        this.written = written;
    }
    @Column(columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    @OneToMany(mappedBy = "book")
    public Set<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Set<Character> characters) {
        this.characters = characters;
    }


    @ManyToMany(mappedBy = "books")
    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }
}
