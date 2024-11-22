package colesico.framework.example.jdbirec;

import colesico.framework.jdbirec.RecordKitApi;
import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordView;

@RecordKit(table = "users", tableAlias = "usr"

)
public interface UserDao extends RecordKitApi<User> {

}
