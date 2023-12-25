package service;

import model.Activity;
import model.dto.CodeMessageObject;
import repository.ActivityRepository;

import java.util.ArrayList;

public class ActivityService {
    private static ActivityService activityService = null;
    private ActivityRepository activityRepository;

    private ActivityService() {
    }

    public static ActivityService getInstance() {
        return activityService == null ? activityService = new ActivityService() : activityService;
    }

    public CodeMessageObject<ArrayList<Activity>> getAll() {
        activityRepository = ActivityRepository.getInstance();
        ArrayList<Activity> activities = activityRepository.getAllActivity();
        return new CodeMessageObject<>(200, "", activities);
    }

    public CodeMessageObject<Activity> getById(String id) {
        activityRepository = ActivityRepository.getInstance();
        CodeMessageObject<Activity> activityCodeMessageObject;
        Activity activity = activityRepository.getActivityById(id);
        if (activity == null) {
            activityCodeMessageObject = new CodeMessageObject<>(400, "Activity Not Found", null);
        } else {
            activityCodeMessageObject = new CodeMessageObject<>(200, "", activity);
        }
        return activityCodeMessageObject;
    }

    public CodeMessageObject<Activity> save(Activity activity) {
        activityRepository = ActivityRepository.getInstance();
        CodeMessageObject<Activity> activityCodeMessageObject;
        if (activity.getId() == null) {
            activity.setId();
            Activity createdActivity = activityRepository.createActivity(activity);
            activityCodeMessageObject = new CodeMessageObject<>(200, "", createdActivity);
        } else {
            activityCodeMessageObject = getById(activity.getId());
            if (activityCodeMessageObject.getCode() == 200) {
                Activity updatedActivity = activityRepository.updateActivity(activity);
                activityCodeMessageObject.setT(updatedActivity);
            }
        }
        return activityCodeMessageObject;
    }

    public CodeMessageObject<Activity> delete(String id) {
        activityRepository = ActivityRepository.getInstance();
        CodeMessageObject<Activity> activityCodeMessageObject = getById(id);
        if (activityCodeMessageObject.getCode() == 200) {
            activityRepository.deleteActivityById(id);
        }
        return activityCodeMessageObject;
    }

    public CodeMessageObject<Activity> getByDescription(String description) {
        activityRepository = ActivityRepository.getInstance();
        CodeMessageObject<Activity> activityCodeMessageObject;
        Activity activity = activityRepository.getActivityByDescription(description);
        if (activity == null) {
            activityCodeMessageObject = new CodeMessageObject<>(400, "Activity Not Found", null);
        } else {
            activityCodeMessageObject = new CodeMessageObject<>(200, "", activity);
        }
        return activityCodeMessageObject;
    }
}
