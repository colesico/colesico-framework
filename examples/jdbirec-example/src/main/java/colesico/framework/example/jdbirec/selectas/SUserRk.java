package colesico.framework.example.jdbirec.selectas;

import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "v_users", tableAlias = "usr")
public interface SUserRk extends RecordKitApi<SUser> {

}
