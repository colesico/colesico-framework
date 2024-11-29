package colesico.framework.example.jdbirec.view;

import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "users_v", tableAlias = "usr")
public interface UserDao extends RecordKitApi<User> {

}
