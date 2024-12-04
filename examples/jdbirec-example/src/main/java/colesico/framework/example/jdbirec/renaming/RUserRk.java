package colesico.framework.example.jdbirec.renaming;


import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "r_users",tableAlias = "usr")
public interface RUserRk extends RecordKitApi<RUser> {
}
