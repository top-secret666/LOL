package by.vstu.zamok.lab4.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "`groups`")
@AttributeOverride(name = "id", column = @Column(name = "gr_id"))
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"students"})
public class Group extends AbstractEntity {
    @Column(unique = true)
    private String grName;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group", cascade = {CascadeType.ALL}, orphanRemoval = true)
//	@ToString.Exclude // Исключаем students из метода toString, чтобы избежать рекурсии
    private Set<Student> students;
}