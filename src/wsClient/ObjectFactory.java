
package wsClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the wsClient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PutFileResponse_QNAME = new QName("http://wsRemoteFileServer/", "putFileResponse");
    private final static QName _RmResponse_QNAME = new QName("http://wsRemoteFileServer/", "rmResponse");
    private final static QName _GetAttrResponse_QNAME = new QName("http://wsRemoteFileServer/", "getAttrResponse");
    private final static QName _RmDir_QNAME = new QName("http://wsRemoteFileServer/", "rmDir");
    private final static QName _GetAttr_QNAME = new QName("http://wsRemoteFileServer/", "getAttr");
    private final static QName _LsResponse_QNAME = new QName("http://wsRemoteFileServer/", "lsResponse");
    private final static QName _PutFile_QNAME = new QName("http://wsRemoteFileServer/", "putFile");
    private final static QName _GetFileResponse_QNAME = new QName("http://wsRemoteFileServer/", "getFileResponse");
    private final static QName _MkDirResponse_QNAME = new QName("http://wsRemoteFileServer/", "mkDirResponse");
    private final static QName _FileAlreadyExistsException_QNAME = new QName("http://wsRemoteFileServer/", "FileAlreadyExistsException");
    private final static QName _AliveResponse_QNAME = new QName("http://wsRemoteFileServer/", "aliveResponse");
    private final static QName _Alive_QNAME = new QName("http://wsRemoteFileServer/", "alive");
    private final static QName _NoSuchPathException_QNAME = new QName("http://wsRemoteFileServer/", "NoSuchPathException");
    private final static QName _MkDir_QNAME = new QName("http://wsRemoteFileServer/", "mkDir");
    private final static QName _GetFile_QNAME = new QName("http://wsRemoteFileServer/", "getFile");
    private final static QName _RmDirResponse_QNAME = new QName("http://wsRemoteFileServer/", "rmDirResponse");
    private final static QName _Rm_QNAME = new QName("http://wsRemoteFileServer/", "rm");
    private final static QName _NoSuchFileException_QNAME = new QName("http://wsRemoteFileServer/", "NoSuchFileException");
    private final static QName _Ls_QNAME = new QName("http://wsRemoteFileServer/", "ls");
    private final static QName _GetFileResponseReturn_QNAME = new QName("", "return");
    private final static QName _PutFileArg1_QNAME = new QName("", "arg1");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: wsClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetFile }
     * 
     */
    public GetFile createGetFile() {
        return new GetFile();
    }

    /**
     * Create an instance of {@link MkDir }
     * 
     */
    public MkDir createMkDir() {
        return new MkDir();
    }

    /**
     * Create an instance of {@link NoSuchPathException }
     * 
     */
    public NoSuchPathException createNoSuchPathException() {
        return new NoSuchPathException();
    }

    /**
     * Create an instance of {@link Rm }
     * 
     */
    public Rm createRm() {
        return new Rm();
    }

    /**
     * Create an instance of {@link NoSuchFileException }
     * 
     */
    public NoSuchFileException createNoSuchFileException() {
        return new NoSuchFileException();
    }

    /**
     * Create an instance of {@link Ls }
     * 
     */
    public Ls createLs() {
        return new Ls();
    }

    /**
     * Create an instance of {@link RmDirResponse }
     * 
     */
    public RmDirResponse createRmDirResponse() {
        return new RmDirResponse();
    }

    /**
     * Create an instance of {@link Alive }
     * 
     */
    public Alive createAlive() {
        return new Alive();
    }

    /**
     * Create an instance of {@link AliveResponse }
     * 
     */
    public AliveResponse createAliveResponse() {
        return new AliveResponse();
    }

    /**
     * Create an instance of {@link MkDirResponse }
     * 
     */
    public MkDirResponse createMkDirResponse() {
        return new MkDirResponse();
    }

    /**
     * Create an instance of {@link FileAlreadyExistsException }
     * 
     */
    public FileAlreadyExistsException createFileAlreadyExistsException() {
        return new FileAlreadyExistsException();
    }

    /**
     * Create an instance of {@link GetAttr }
     * 
     */
    public GetAttr createGetAttr() {
        return new GetAttr();
    }

    /**
     * Create an instance of {@link RmDir }
     * 
     */
    public RmDir createRmDir() {
        return new RmDir();
    }

    /**
     * Create an instance of {@link GetAttrResponse }
     * 
     */
    public GetAttrResponse createGetAttrResponse() {
        return new GetAttrResponse();
    }

    /**
     * Create an instance of {@link RmResponse }
     * 
     */
    public RmResponse createRmResponse() {
        return new RmResponse();
    }

    /**
     * Create an instance of {@link GetFileResponse }
     * 
     */
    public GetFileResponse createGetFileResponse() {
        return new GetFileResponse();
    }

    /**
     * Create an instance of {@link PutFile }
     * 
     */
    public PutFile createPutFile() {
        return new PutFile();
    }

    /**
     * Create an instance of {@link LsResponse }
     * 
     */
    public LsResponse createLsResponse() {
        return new LsResponse();
    }

    /**
     * Create an instance of {@link PutFileResponse }
     * 
     */
    public PutFileResponse createPutFileResponse() {
        return new PutFileResponse();
    }

    /**
     * Create an instance of {@link FileInfo }
     * 
     */
    public FileInfo createFileInfo() {
        return new FileInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PutFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "putFileResponse")
    public JAXBElement<PutFileResponse> createPutFileResponse(PutFileResponse value) {
        return new JAXBElement<PutFileResponse>(_PutFileResponse_QNAME, PutFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RmResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "rmResponse")
    public JAXBElement<RmResponse> createRmResponse(RmResponse value) {
        return new JAXBElement<RmResponse>(_RmResponse_QNAME, RmResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttrResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "getAttrResponse")
    public JAXBElement<GetAttrResponse> createGetAttrResponse(GetAttrResponse value) {
        return new JAXBElement<GetAttrResponse>(_GetAttrResponse_QNAME, GetAttrResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RmDir }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "rmDir")
    public JAXBElement<RmDir> createRmDir(RmDir value) {
        return new JAXBElement<RmDir>(_RmDir_QNAME, RmDir.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAttr }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "getAttr")
    public JAXBElement<GetAttr> createGetAttr(GetAttr value) {
        return new JAXBElement<GetAttr>(_GetAttr_QNAME, GetAttr.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "lsResponse")
    public JAXBElement<LsResponse> createLsResponse(LsResponse value) {
        return new JAXBElement<LsResponse>(_LsResponse_QNAME, LsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PutFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "putFile")
    public JAXBElement<PutFile> createPutFile(PutFile value) {
        return new JAXBElement<PutFile>(_PutFile_QNAME, PutFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "getFileResponse")
    public JAXBElement<GetFileResponse> createGetFileResponse(GetFileResponse value) {
        return new JAXBElement<GetFileResponse>(_GetFileResponse_QNAME, GetFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MkDirResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "mkDirResponse")
    public JAXBElement<MkDirResponse> createMkDirResponse(MkDirResponse value) {
        return new JAXBElement<MkDirResponse>(_MkDirResponse_QNAME, MkDirResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileAlreadyExistsException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "FileAlreadyExistsException")
    public JAXBElement<FileAlreadyExistsException> createFileAlreadyExistsException(FileAlreadyExistsException value) {
        return new JAXBElement<FileAlreadyExistsException>(_FileAlreadyExistsException_QNAME, FileAlreadyExistsException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AliveResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "aliveResponse")
    public JAXBElement<AliveResponse> createAliveResponse(AliveResponse value) {
        return new JAXBElement<AliveResponse>(_AliveResponse_QNAME, AliveResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Alive }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "alive")
    public JAXBElement<Alive> createAlive(Alive value) {
        return new JAXBElement<Alive>(_Alive_QNAME, Alive.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchPathException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "NoSuchPathException")
    public JAXBElement<NoSuchPathException> createNoSuchPathException(NoSuchPathException value) {
        return new JAXBElement<NoSuchPathException>(_NoSuchPathException_QNAME, NoSuchPathException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MkDir }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "mkDir")
    public JAXBElement<MkDir> createMkDir(MkDir value) {
        return new JAXBElement<MkDir>(_MkDir_QNAME, MkDir.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "getFile")
    public JAXBElement<GetFile> createGetFile(GetFile value) {
        return new JAXBElement<GetFile>(_GetFile_QNAME, GetFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RmDirResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "rmDirResponse")
    public JAXBElement<RmDirResponse> createRmDirResponse(RmDirResponse value) {
        return new JAXBElement<RmDirResponse>(_RmDirResponse_QNAME, RmDirResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Rm }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "rm")
    public JAXBElement<Rm> createRm(Rm value) {
        return new JAXBElement<Rm>(_Rm_QNAME, Rm.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchFileException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "NoSuchFileException")
    public JAXBElement<NoSuchFileException> createNoSuchFileException(NoSuchFileException value) {
        return new JAXBElement<NoSuchFileException>(_NoSuchFileException_QNAME, NoSuchFileException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ls }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wsRemoteFileServer/", name = "ls")
    public JAXBElement<Ls> createLs(Ls value) {
        return new JAXBElement<Ls>(_Ls_QNAME, Ls.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetFileResponse.class)
    public JAXBElement<byte[]> createGetFileResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_GetFileResponseReturn_QNAME, byte[].class, GetFileResponse.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg1", scope = PutFile.class)
    public JAXBElement<byte[]> createPutFileArg1(byte[] value) {
        return new JAXBElement<byte[]>(_PutFileArg1_QNAME, byte[].class, PutFile.class, ((byte[]) value));
    }

}
