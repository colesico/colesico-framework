package colesico.framework.example.ioc.message;

import colesico.framework.ioc.Supplier;

public class MainBeanMSG {

    private ReceiverBean receiver;

    /**
     * Use the Supplier injection to have ability to pass message to RecieverBean to its constructor
     * @see Supplier
     * @param recieverSup
     */
    public MainBeanMSG(Supplier<ReceiverBean> recieverSup) {
        TextMessage msg = new TextMessage("MainBeanMSG");
        this.receiver = recieverSup.get(msg);
    }

    public String getMessageText(){
        return receiver.getMessage().getText();
    }
}
