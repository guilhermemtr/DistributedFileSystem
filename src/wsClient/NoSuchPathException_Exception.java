
package wsClient;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "NoSuchPathException", targetNamespace = "http://wsRemoteFileServer/")
public class NoSuchPathException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private NoSuchPathException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public NoSuchPathException_Exception(String message, NoSuchPathException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public NoSuchPathException_Exception(String message, NoSuchPathException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: wsClient.NoSuchPathException
     */
    public NoSuchPathException getFaultInfo() {
        return faultInfo;
    }

}