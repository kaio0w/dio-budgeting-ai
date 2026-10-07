package dio.budgeting.application;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;

import java.util.List;

public class ListTransactionsUseCase {
    private final TransactionRepository repository;

    public ListTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> execute() {
        return repository.findAll();
    }
}
