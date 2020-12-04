package colesico.framework.rpc.teleapi;

/**
 * Basic rpc request and response extension
 * Used to provide ability to pass principal and profile objects
 */
public interface BasicEnvelope {

    /**
     * Serialized principal
     */
    byte[] getPrincipal();

    void setPrincipal(byte[] principal);

    /**
     * Serialized profile
     */
    byte[] getProfile();

    void setProfile(byte[] profile);
}
