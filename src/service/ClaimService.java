package service;

import model.Budget;
import model.Claim;
import model.dto.CodeMessageObject;
import repository.ClaimRepository;
import util.GlobalVariable;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class ClaimService {
    private static ClaimService claimService = null;
    private ClaimRepository claimRepository;
    private UserService userService;
    private ActivityService activityService;
    private BudgetService budgetService;

    public ClaimService() {
    }

    public static ClaimService getInstance() {
        return claimService == null ? claimService = new ClaimService() : claimService;
    }

    public CodeMessageObject<ArrayList<Claim>> getAll() {
        claimRepository = ClaimRepository.getInstance();
        ArrayList<Claim> claims = claimRepository.getAllClaim();
        return new CodeMessageObject<>(200, "", claims);
    }

    public CodeMessageObject<Claim> getById(String id) {
        claimRepository = ClaimRepository.getInstance();
        CodeMessageObject<Claim> claimCodeMessageObject;
        Claim claim = claimRepository.getClaimById(id);
        if (claim == null) {
            claimCodeMessageObject = new CodeMessageObject<>(400, "Claim Not Found", null);
        } else {
            claimCodeMessageObject = new CodeMessageObject<>(200, "", claim);
        }
        return claimCodeMessageObject;
    }

    public CodeMessageObject<Claim> save(Claim claim, String activityDescription, String budgetDescription, String userName) {
        claim.setDate();
        claimRepository = ClaimRepository.getInstance();
        userService = UserService.getInstance();
        activityService = ActivityService.getInstance();
        budgetService = BudgetService.getInstance();
        claim.setActivity(activityService.getByDescription(activityDescription).getT());
        claim.setActivityId(claim.getActivity().getId());
        claim.setBudget(budgetService.getByDescription(budgetDescription).getT());
        claim.setBudgetId(claim.getBudget().getId());
        CodeMessageObject<Claim> claimCodeMessageObject;
        if (claim.getId() == null) {
            claim.setId();
            claim.setUser(GlobalVariable.currentUser);
            claim.setUserId(claim.getUser().getId());
            Claim createdClaim = claimRepository.createClaim(claim);
            claimCodeMessageObject = new CodeMessageObject<>(200, "", createdClaim);
        } else {
            claim.setUser(userService.getByName(userName).getT());
            claim.setUserId(claim.getUser().getId());
            claimCodeMessageObject = getById(claim.getId());
            if(claim.getStatus().equals("Confirmed") || claim.getStatus().equals("Rejected")){
                claimCodeMessageObject = new CodeMessageObject<>(400, "Confirmed and Rejected Claim cannot be updated!", null);
            }
            if (claimCodeMessageObject.getCode() == 200) {
                Claim updatedClaim = claimRepository.updateClaim(claim);
                claimCodeMessageObject.setT(updatedClaim);
            }
        }
        return claimCodeMessageObject;
    }

    public CodeMessageObject<Claim> delete(String id) {
        claimRepository = ClaimRepository.getInstance();
        CodeMessageObject<Claim> claimCodeMessageObject = getById(id);
        if (claimCodeMessageObject.getCode() == 200) {
            claimRepository.deleteById(id);
        }
        return claimCodeMessageObject;
    }

    public CodeMessageObject<Claim> getByDateAndStatusAndRemark(Timestamp date, String status, String remark) {
        claimRepository = ClaimRepository.getInstance();
        CodeMessageObject<Claim> claimCodeMessageObject;
        Claim claim = claimRepository.getClaimByDateAndStatusAndRemark(date, status, remark);
        if (claim == null) {
            claimCodeMessageObject = new CodeMessageObject<>(400, "Claim Not Found", null);
        } else {
            claimCodeMessageObject = new CodeMessageObject<>(200, "", claim);
        }
        return claimCodeMessageObject;
    }

    public CodeMessageObject<Claim> updateStatus(String id, String status) {
        claimRepository = ClaimRepository.getInstance();
        budgetService = BudgetService.getInstance();
        CodeMessageObject<Claim> claimCodeMessageObject = getById(id);
        Claim claim;
        Budget budget;
        if (claimCodeMessageObject.getCode() == 200) {
            claim = claimCodeMessageObject.getT();
            budget = budgetService.getById(claim.getBudgetId()).getT();
            if(status.equals("Confirmed")) {
                budget.setAmount(budget.getAmount() - claim.getAmount());
                budgetService.save(budget);
            }
            claimRepository.updateClaimStatus(id, Timestamp.from(Instant.now()), status, GlobalVariable.currentUser.getId());
        }
        return claimCodeMessageObject;
    }
}
