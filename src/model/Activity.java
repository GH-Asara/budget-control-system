package model;

import java.util.Objects;
import java.util.UUID;

public class Activity {
    private String id;
    private String description;
    private String activityType;

    public Activity(String id, String description, String activityType) {
        this.id = id;
        this.description = description;
        this.activityType = activityType;
    }

    public Activity(String description, String activityType) {
        this.description = description;
        this.activityType = activityType;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(id, activity.id) && Objects.equals(description, activity.description) && Objects.equals(activityType, activity.activityType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, activityType);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", activityType='" + activityType + '\'' +
                '}';
    }
}
