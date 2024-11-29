package colesico.framework.example.jdbirec.renaming;


import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "users_r")
public interface UserRK extends RecordKitApi<User> {
}
