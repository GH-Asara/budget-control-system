package model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Claim {
    private String id;
    private Timestamp date;
    private String status;
    private String remark;
    private Activity activity;
    private String activityId;
    private Budget budget;
    private String budgetId;
    private User user;
    private String userId;
    private User approval;
    private String approvalId;
    private Integer amount;

    public Claim(String id, Timestamp date, String status, String remark, String activityId, String budgetId, String userId, String approvalId, Integer amount) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.remark = remark;
        this.activityId = activityId;
        this.budgetId = budgetId;
        this.userId = userId;
        this.approvalId = approvalId;
        this.amount = amount;
    }

    public Claim(String id, Timestamp date, String status, String remark, Activity activity, Budget budget, User user, User approval, Integer amount) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.remark = remark;
        this.activity = activity;
        this.budget = budget;
        this.user = user;
        this.approval = approval;
        this.amount = amount;
    }

    public Claim(String status, String remark, String activityId, String budgetId, Integer amount) {
        this.status = status;
        this.remark = remark;
        this.activityId = activityId;
        this.budgetId = budgetId;
        this.approvalId = "";
        this.amount = amount;
    }

    public Claim(String status, String remark, Activity activity, Budget budget, Integer amount) {
        this.status = status;
        this.remark = remark;
        this.activity = activity;
        this.budget = budget;
        this.amount = amount;
    }

    public Claim(String status, String remark, Integer amount) {
        this.status = status;
        this.remark = remark;
        this.amount = amount;
    }

    public Claim(String id, String status, String remark, Integer amount) {
        this.id = id;
        this.status = status;
        this.remark = remark;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate() {
        this.date = Timestamp.from(Instant.now());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getApproval() {
        return approval;
    }

    public void setApproval(User approval) {
        this.approval = approval;
    }

    public String getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(String approvalId) {
        this.approvalId = approvalId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim claim = (Claim) o;
        return Objects.equals(id, claim.id) && Objects.equals(date, claim.date) && Objects.equals(status, claim.status) && Objects.equals(remark, claim.remark) && Objects.equals(activity, claim.activity) && Objects.equals(activityId, claim.activityId) && Objects.equals(budget, claim.budget) && Objects.equals(budgetId, claim.budgetId) && Objects.equals(user, claim.user) && Objects.equals(userId, claim.userId) && Objects.equals(approval, claim.approval) && Objects.equals(approvalId, claim.approvalId) && Objects.equals(amount, claim.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, status, remark, activity, activityId, budget, budgetId, user, userId, approval, approvalId, amount);
    }

    @Override
    public String toString() {
        return "Claim{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", remark='" + remark + '\'' +
                ", activity=" + activity +
                ", budget=" + budget +
                ", user=" + user +
                ", approval=" + approval +
                ", amount=" + amount +
                '}';
    }
}
