package dio.budgeting.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface SpringDataTransactionRepository extends JpaRepository<TransactionJpaEntity, UUID> {
    List<TransactionJpaEntity> findByCategoryIgnoreCaseOrderByOccurredAtDesc(String category);
}
