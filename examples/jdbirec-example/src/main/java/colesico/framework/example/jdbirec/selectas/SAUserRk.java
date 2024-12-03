package colesico.framework.example.jdbirec.selectas;

import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "users_v", tableAlias = "usr")
public interface SAUserRk extends RecordKitApi<SAUser> {

}
