package by.timofeyzaytsev.textcorrector.entity;


import by.timofeyzaytsev.textcorrector.entity.enums.Language;
import by.timofeyzaytsev.textcorrector.entity.enums.TextPolishTaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Builder
@Table(name = "text_polish_tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TextPolishTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Language language;

    private String errorDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TextPolishTaskStatus status;
}
