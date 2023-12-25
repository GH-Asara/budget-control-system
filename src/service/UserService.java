package service;

import model.User;
import model.dto.CodeMessageObject;
import model.request.LoginRequest;
import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepository;
import util.GlobalVariable;

public class UserService {
    private static UserService userService = null;
    private UserRepository userRepository;

    private UserService() {
    }

    public static UserService getInstance() {
        return userService == null ? userService = new UserService() : userService;
    }

    public CodeMessageObject<User> register(User user) {
        userRepository = UserRepository.getInstance();
        CodeMessageObject<User> userCodeMessageObject;
        user.setId();
        if (userRepository.getUserByUsername(user.getUsername()) != null) {
            userCodeMessageObject = new CodeMessageObject<>(400, "Name Already Used", user);
        } else {
            User createdUser = userRepository.createUser(user);
            userCodeMessageObject = new CodeMessageObject<>(200, "", createdUser);
            GlobalVariable.currentUser = userCodeMessageObject.getT();
        }
        return userCodeMessageObject;
    }

    public CodeMessageObject<User> login(LoginRequest loginRequest) {
        userRepository = UserRepository.getInstance();
        CodeMessageObject<User> userCodeMessageObject;
        User user = userRepository.getUserByUsername(loginRequest.getUsername());
        if (user == null) {
            userCodeMessageObject = new CodeMessageObject<>(400, "Wrong Username", null);
        } else if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            userCodeMessageObject = new CodeMessageObject<>(400, "Wrong Password", user);
        } else {
            userCodeMessageObject = new CodeMessageObject<>(200, "", user);
            GlobalVariable.currentUser = userCodeMessageObject.getT();
        }
        return userCodeMessageObject;
    }

    public CodeMessageObject<User> getById(String id) {
        userRepository = UserRepository.getInstance();
        CodeMessageObject<User> userCodeMessageObject;
        User user = userRepository.getUserById(id);
        if (user == null) {
            userCodeMessageObject = new CodeMessageObject<>(400, "User NOt Found", null);
        } else {
            userCodeMessageObject = new CodeMessageObject<>(200, "", user);
            GlobalVariable.currentUser = userCodeMessageObject.getT();
        }
        return userCodeMessageObject;
    }

    public CodeMessageObject<User> getByName(String name) {
        userRepository = UserRepository.getInstance();
        CodeMessageObject<User> userCodeMessageObject;
        User user = userRepository.getUserByName(name);
        if (user == null) {
            userCodeMessageObject = new CodeMessageObject<>(400, "User NOt Found", null);
        } else {
            userCodeMessageObject = new CodeMessageObject<>(200, "", user);
            GlobalVariable.currentUser = userCodeMessageObject.getT();
        }
        return userCodeMessageObject;
    }
}
