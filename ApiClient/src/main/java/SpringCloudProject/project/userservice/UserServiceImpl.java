package SpringCloudProject.project.userservice;

import SpringCloudProject.project.shared.Utils;
import SpringCloudProject.project.ui.model.request.UserDetailsRequestModel;
import SpringCloudProject.project.ui.model.response.UserRest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {
    Map<String,UserRest> users;

    Utils utils;

    public UserServiceImpl(){}

    public UserServiceImpl(Utils utils){
        this.utils=utils;
    }
    @Override
    public UserRest createUser(UserDetailsRequestModel model) {
        UserRest returnValue = new UserRest();

        returnValue.setEmail(model.getEmail());
        returnValue.setFirstName(model.getFirstName());
        returnValue.setLastName(model.getLastName());
        String userId = utils.generateUserId();
        returnValue.setUserId(userId);
        if (users == null) {
            users = new HashMap<>();
        }

        users.put(userId, returnValue);
return returnValue;
    }
}
