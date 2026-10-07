package dio.budgeting.infrastructure.persistence;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaTransactionRepositoryAdapter implements TransactionRepository {
    private final SpringDataTransactionRepository repository;

    public JpaTransactionRepositoryAdapter(SpringDataTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return toDomain(repository.save(toEntity(transaction)));
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Transaction> findByCategoryIgnoreCase(String category) {
        return repository.findByCategoryIgnoreCaseOrderByOccurredAtDesc(category)
                .stream().map(this::toDomain).toList();
    }

    private TransactionJpaEntity toEntity(Transaction t) {
        return new TransactionJpaEntity(t.getId(), t.getType(), t.getAmount(), t.getCategory(),
                t.getDescription(), t.getOccurredAt());
    }

    private Transaction toDomain(TransactionJpaEntity e) {
        return new Transaction(e.getId(), e.getType(), e.getAmount(), e.getCategory(),
                e.getDescription(), e.getOccurredAt());
    }
}
