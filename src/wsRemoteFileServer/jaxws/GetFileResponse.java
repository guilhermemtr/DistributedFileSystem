
package wsRemoteFileServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getFileResponse", namespace = "http://wsRemoteFileServer/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFileResponse", namespace = "http://wsRemoteFileServer/")
public class GetFileResponse {

    @XmlElement(name = "return", namespace = "", nillable = true)
    private byte[] _return;

    /**
     * 
     * @return
     *     returns byte[]
     */
    public byte[] getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(byte[] _return) {
        this._return = _return;
    }

}
