package dio.budgeting.application;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.domain.TransactionType;

import java.math.BigDecimal;

public class CreateTransactionUseCase {
    private final TransactionRepository repository;

    public CreateTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction execute(TransactionType type, BigDecimal amount, String category, String description) {
        return repository.save(Transaction.create(type, amount, category, description));
    }
}
