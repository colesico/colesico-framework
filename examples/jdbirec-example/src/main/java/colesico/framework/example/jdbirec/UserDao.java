package colesico.framework.example.jdbirec;

import colesico.framework.jdbirec.RecordKitApi;
import colesico.framework.jdbirec.RecordKit;

@RecordKit(table = "users", tableAlias = "usr"

)
public interface UserDao extends RecordKitApi<User> {

}
