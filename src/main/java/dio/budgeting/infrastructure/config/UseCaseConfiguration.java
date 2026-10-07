package dio.budgeting.infrastructure.config;

import dio.budgeting.application.CreateTransactionUseCase;
import dio.budgeting.application.GetBalanceUseCase;
import dio.budgeting.application.GetExpensesByCategoryUseCase;
import dio.budgeting.application.ListTransactionsUseCase;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {
    @Bean
    CreateTransactionUseCase createTransactionUseCase(TransactionRepository repository) {
        return new CreateTransactionUseCase(repository);
    }

    @Bean
    ListTransactionsUseCase listTransactionsUseCase(TransactionRepository repository) {
        return new ListTransactionsUseCase(repository);
    }

    @Bean
    GetExpensesByCategoryUseCase getExpensesByCategoryUseCase(TransactionRepository repository) {
        return new GetExpensesByCategoryUseCase(repository);
    }

    @Bean
    GetBalanceUseCase getBalanceUseCase(TransactionRepository repository) {
        return new GetBalanceUseCase(repository);
    }
}
