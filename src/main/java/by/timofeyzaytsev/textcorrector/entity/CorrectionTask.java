package by.timofeyzaytsev.textcorrector.entity;


import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskLanguage;
import by.timofeyzaytsev.textcorrector.entity.enums.CorrectionTaskStatus;
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
@Table(name = "correction_tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CorrectionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private CorrectionTaskLanguage language;

    @Enumerated(EnumType.STRING)
    private CorrectionTaskStatus status;
}
