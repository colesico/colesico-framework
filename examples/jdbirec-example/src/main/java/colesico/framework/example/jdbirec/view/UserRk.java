package colesico.framework.example.jdbirec.view;

import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "users", tableAlias = "usr")
public interface UserRk extends RecordKitApi<User> {

}
