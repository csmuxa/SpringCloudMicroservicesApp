package SpringCloudProject.project.userservice;

import SpringCloudProject.project.ui.model.request.UserDetailsRequestModel;
import SpringCloudProject.project.ui.model.response.UserRest;

public interface UserService {
    UserRest createUser(UserDetailsRequestModel model);
}
