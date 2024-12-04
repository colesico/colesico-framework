package colesico.framework.example.jdbirec.join;

import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "users_v", tableAlias = "usr")
public interface JUserRk extends RecordKitApi<JUser> {

}