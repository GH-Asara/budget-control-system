package service;

import model.Budget;
import model.dto.CodeMessageObject;
import repository.BudgetRepository;

import java.util.ArrayList;

public class BudgetService {
    private static BudgetService budgetService = null;
    private BudgetRepository budgetRepository;

    public BudgetService() {
    }

    public static BudgetService getInstance() {
        return budgetService == null ? budgetService = new BudgetService() : budgetService;
    }

    public CodeMessageObject<ArrayList<Budget>> getAll() {
        budgetRepository = BudgetRepository.getInstance();
        ArrayList<Budget> budgets = budgetRepository.getAllBudget();
        return new CodeMessageObject<>(200, "", budgets);
    }

    public CodeMessageObject<Budget> getById(String id) {
        budgetRepository = BudgetRepository.getInstance();
        CodeMessageObject<Budget> budgetCodeMessageObject;
        Budget budget = budgetRepository.getBudgetById(id);
        if (budget == null) {
            budgetCodeMessageObject = new CodeMessageObject<>(400, "Budget Not Found", null);
        } else {
            budgetCodeMessageObject = new CodeMessageObject<>(200, "", budget);
        }
        return budgetCodeMessageObject;
    }

    public CodeMessageObject<Budget> save(Budget budget) {
        budgetRepository = BudgetRepository.getInstance();
        CodeMessageObject<Budget> budgetCodeMessageObject;
        if (budget.getId() == null) {
            budget.setId();
            Budget createdBudget = budgetRepository.createBudget(budget);
            budgetCodeMessageObject = new CodeMessageObject<>(200, "", createdBudget);
        } else {
            budgetCodeMessageObject = getById(budget.getId());
            if (budgetCodeMessageObject.getCode() == 200) {
                Budget updatedBudget = budgetRepository.updateBudget(budget);
                budgetCodeMessageObject.setT(updatedBudget);
            }
        }
        return budgetCodeMessageObject;
    }

    public CodeMessageObject<Budget> delete(String id) {
        budgetRepository = BudgetRepository.getInstance();
        CodeMessageObject<Budget> budgetCodeMessageObject = getById(id);
        if (budgetCodeMessageObject.getCode() == 200) {
            budgetRepository.deleteBudgetById(id);
        }
        return budgetCodeMessageObject;
    }

    public CodeMessageObject<Budget> getByDescription(String description) {
        budgetRepository = BudgetRepository.getInstance();
        CodeMessageObject<Budget> budgetCodeMessageObject;
        Budget budget = budgetRepository.getBudgetByDescription(description);
        if (budget == null) {
            budgetCodeMessageObject = new CodeMessageObject<>(400, "Activity Not Found", null);
        } else {
            budgetCodeMessageObject = new CodeMessageObject<>(200, "", budget);
        }
        return budgetCodeMessageObject;
    }
}
