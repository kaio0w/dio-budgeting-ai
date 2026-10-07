package dio.budgeting.infrastructure.ai;

import dio.budgeting.application.CreateTransactionUseCase;
import dio.budgeting.application.GetBalanceUseCase;
import dio.budgeting.application.GetExpensesByCategoryUseCase;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionType;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FinanceTools {
    private final CreateTransactionUseCase create;
    private final GetExpensesByCategoryUseCase byCategory;
    private final GetBalanceUseCase balance;

    public FinanceTools(CreateTransactionUseCase create, GetExpensesByCategoryUseCase byCategory,
                        GetBalanceUseCase balance) {
        this.create = create;
        this.byCategory = byCategory;
        this.balance = balance;
    }

    @Tool(description = "Cria uma transação financeira de receita ou despesa")
    public String createTransaction(
            @ToolParam(description = "Tipo da transação: INCOME para receita ou EXPENSE para despesa") String type,
            @ToolParam(description = "Valor positivo da transação") BigDecimal amount,
            @ToolParam(description = "Categoria, por exemplo alimentação, transporte ou salário") String category,
            @ToolParam(description = "Descrição curta da transação") String description) {

        TransactionType parsedType = TransactionType.valueOf(type.trim().toUpperCase());
        Transaction t = create.execute(parsedType, amount, category, description);
        return "Transação criada com sucesso. Id: %s, tipo: %s, valor: R$ %s, categoria: %s."
                .formatted(t.getId(), t.getType(), t.getAmount(), t.getCategory());
    }

    @Tool(description = "Consulta quanto foi gasto em uma categoria")
    public String getExpensesByCategory(
            @ToolParam(description = "Categoria que será consultada") String category) {
        var result = byCategory.execute(category);
        return "Na categoria %s foram encontradas %d despesas, totalizando R$ %s."
                .formatted(result.category(), result.quantity(), result.total());
    }

    @Tool(description = "Consulta o saldo atual calculado como receitas menos despesas")
    public String getBalance() {
        return "O saldo atual é R$ %s.".formatted(balance.execute());
    }
}
