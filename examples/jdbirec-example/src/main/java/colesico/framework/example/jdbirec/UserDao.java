package colesico.framework.example.jdbirec;

import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitConfig;
import colesico.framework.jdbirec.RecordView;

@RecordKitConfig(table = "ctg_embr",
        tableAlias = "embroidery",
        views = {RecordView.FULL_RECORD, RecordView.BRIEF_RECORD})
public interface UserDao extends RecordKit<User>{

}
