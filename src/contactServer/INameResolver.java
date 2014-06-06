package contactServer;

public interface INameResolver {

	void registerServer(String name, String url);
	
	void removeServer(String name);
	
	String getUrl(String name);
	
}
