package colesico.framework.example.jdbirec;

import colesico.framework.jdbirec.RecordKitApi;
import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordView;

@RecordKit(table = "users", tableAlias = "usr",
        viewsStr = {RecordView.FULL_VIEW, RecordView.BRIEF_VIEW})
public interface UserDao extends RecordKitApi<User> {

}
