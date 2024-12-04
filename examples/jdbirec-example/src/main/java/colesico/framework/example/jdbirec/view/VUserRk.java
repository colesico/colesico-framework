package colesico.framework.example.jdbirec.view;

import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "v_users", tableAlias = "usr")
public interface VUserRk extends RecordKitApi<VUser> {

}
