package colesico.framework.rpc.teleapi;

/**
 * Default envelope extension
 */
public interface RpcEnvelope {

    /**
     * Serialized principal
     */
    byte[] getPrincipal();

    void setPrincipal(byte[] principal);

    /**
     * Serialized profile
     */
    byte[] getProfile();

    void setProfile(byte profile);
}
