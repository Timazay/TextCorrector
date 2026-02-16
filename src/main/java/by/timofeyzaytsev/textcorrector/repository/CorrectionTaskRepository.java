package by.timofeyzaytsev.textcorrector.repository;

import by.timofeyzaytsev.textcorrector.entity.CorrectionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorrectionTaskRepository extends JpaRepository<CorrectionTask, UUID> {

    @Query("SELECT ct FROM CorrectionTask ct WHERE ct.status IN ('NEW_TASK', 'PROCCESSING')")
    List<CorrectionTask> findByStatus();

    Optional<CorrectionTask> findCorrectionTaskById(UUID id);
}
