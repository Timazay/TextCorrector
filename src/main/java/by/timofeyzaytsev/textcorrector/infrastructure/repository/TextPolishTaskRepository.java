package by.timofeyzaytsev.textcorrector.infrastructure.repository;

import by.timofeyzaytsev.textcorrector.infrastructure.entity.TextPolishTask;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TextPolishTaskRepository extends JpaRepository<TextPolishTask, UUID> {

    @Query("SELECT tpt FROM TextPolishTask tpt WHERE tpt.status = 'PROCESSING'")
    List<TextPolishTask> findByProcessingStatus(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")})
    @Query("SELECT tpt FROM TextPolishTask tpt WHERE tpt.status = 'NEW_TASK'")
    List<TextPolishTask> findAllNewTasks(Pageable pageable);

    Optional<TextPolishTask> findCorrectionTaskById(UUID id);
}
