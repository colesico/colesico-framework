package colesico.framework.example.jdbirec.helloworld;


import colesico.framework.jdbirec.RecordKit;
import colesico.framework.jdbirec.RecordKitApi;

@RecordKit(table = "users")
public interface UserRk extends RecordKitApi<User> {
}