package colesico.framework.example.jdbirec.helloworld;


import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "users_h")
public interface UserRK extends RecordKitApi<User> {
}
