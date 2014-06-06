
package wsRemoteFileServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "putFile", namespace = "http://wsRemoteFileServer/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "putFile", namespace = "http://wsRemoteFileServer/", propOrder = {
    "arg0",
    "arg1"
})
public class PutFile {

    @XmlElement(name = "arg0", namespace = "")
    private String arg0;
    @XmlElement(name = "arg1", namespace = "", nillable = true)
    private byte[] arg1;

    /**
     * 
     * @return
     *     returns String
     */
    public String getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    /**
     * 
     * @return
     *     returns byte[]
     */
    public byte[] getArg1() {
        return this.arg1;
    }

    /**
     * 
     * @param arg1
     *     the value for the arg1 property
     */
    public void setArg1(byte[] arg1) {
        this.arg1 = arg1;
    }

}
