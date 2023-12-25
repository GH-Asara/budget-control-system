package util;

import model.Activity;
import model.Budget;
import model.Claim;
import model.User;

import java.util.ArrayList;

public class GlobalVariable {
    public static ArrayList<String> roles = new ArrayList<>() {
        {
            add("Super Admin");
            add("Admin");
            add("Departement Head");
            add("Officer");
        }
    };
    public static ArrayList<String> activityTypes = new ArrayList<>() {
        {
            add("Promosi");
            add("Diskon");
            add("Free Product");
            add("Sewa");
            add("Sponsorship");
            add("Event");
            add("Transportasi");
        }
    };
    public static ArrayList<String> docStatus = new ArrayList<>() {
        {
            add("Confirm To Approve");
            add("Confirmed");
            add("Rejected");
        }
    };
    public static User currentUser = null;
    public static ArrayList<Activity> activities = new ArrayList<>();
    public static ArrayList<Budget> budgets = new ArrayList<>();
    public static ArrayList<Claim> claims = new ArrayList<>();
    public static ArrayList<String> activityDescriptions = new ArrayList<>();
    public static ArrayList<String> budgetDescriptions = new ArrayList<>();
}
