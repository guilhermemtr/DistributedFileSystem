package fileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import utils.Logger;
import exceptions.FileAlreadyExistsException;
import exceptions.NoSuchFileException;
import exceptions.NoSuchPathException;


public class GoogleDriveProxyFileSystem implements IFileSystem {

	public static final String NODIR = "Directory does not exist.";
	public static final String ALREADYEXISTSDIR = "Directory already exists.";
	public static final String[] FILEALREADYEXISTS = { "File ",
			" already exists." };
	public static final String NOSUCHFILENAME = "File not found ";
	public static final String PROBLEMWITHFILE = "Problem handling ";
	public static final String READ = "r";
	public static final String WRITE = "rw";

	private static final long serialVersionUID = 6810600069166858611L;

	private OAuthService service;
	private Token accessToken;

	public GoogleDriveProxyFileSystem(OAuthService service, Token accessToken) {
		super();
		this.accessToken = accessToken;
		this.service = service;
	}
	
	//works
	/**
	 * Generalização de um pedido REST genérico à API da Google Drive.
	 * @param httpRequest - método do pedido (GET, POST, PUT, etc.).
	 * @param link - extensão do URL para execução de um pedido (e.g. '/files' lista os ficheiros todos.
	 * @return um objecto em JSON da resposta ao pedido, validada ou excepção caso contrário.
	 * @throws Exception - Código de resposta != 200
	 */
	private JSONObject serviceRequest(Verb httpRequest, String link) {
		
		OAuthRequest request = new OAuthRequest(httpRequest, link);
		service.signRequest(accessToken, request);
		Response response = request.send();
		
		if (response.getCode() != 200)
			return null;

		JSONParser parser = new JSONParser();
		JSONObject res;
		try {
			res = (JSONObject) parser.parse(response.getBody());
		} catch (ParseException e) {
			return null;
		}
		
		return res;
	}
	
	//works
	/**
	 * Método genérico para obter o ID de um ficheiro/pasta ao longo de um caminho.
	 * @param path - caminho do ficheiro/pasta
	 * @return o ID do ficheiro/pasta
	 * @throws NoSuchPathException - caminho inválido.
	 */
	public String getID(String path) throws NoSuchPathException {
		path = parser(path);
		if(path.compareTo("") == 0)
			return "root";
		else
		{
			JSONObject res;
			res = serviceRequest(Verb.GET,
					"https://www.googleapis.com/drive/v2/files/root/children");
			JSONArray files = (JSONArray) res.get("items");
			String[] dirs = path.split("/");
			String folderID = "";
			boolean validPath = false;
			List<String> ids = new LinkedList<String>();
			
			for(int i = 0; i < dirs.length; i++)
			{
				try {
					
					for(Object o: files)
						ids.add((String) ((JSONObject) o).get("id"));
					
					for(String id: ids)
					{
						res = serviceRequest(Verb.GET,
								"https://www.googleapis.com/drive/v2/files/" + id);
						
						if(res.get("title").equals(dirs[i]))
						{
							validPath = true;
							folderID = id;
							break;
						}
					}
					
					if(validPath)
						validPath = false;
					else
						throw new Exception();
					
					ids.clear();
					if(i == dirs.length - 1)
						return folderID;
					
					res = serviceRequest(Verb.GET,
							"https://www.googleapis.com/drive/v2/files/" +
							folderID + "/children");
					files = (JSONArray) res.get("items");
				} catch (Exception e) {
					throw new NoSuchPathException("Invalid path.");
				}
			}
			return folderID;
		}
	}

	//works
	/**
	 * @param path - Caminho do sistema de ficheiros.
	 * @throws NoSuchPathException - Caminho inválido.
	 * @returns uma lista de ficheiros ou pastas na directoria alvo.
	 */
	@Override
	public String[] ls(String path) throws NoSuchPathException {
		
		path = getID(path);
		JSONObject res;
		res = serviceRequest(Verb.GET, "https://www.googleapis.com/drive/v2/files/" + path + "/children");
		
		List<String> fileList = new LinkedList<String>();
		JSONArray items = (JSONArray) res.get("items");
		
		@SuppressWarnings("rawtypes")
		Iterator it = items.iterator();
		while (it.hasNext()) {
			JSONObject file = (JSONObject) it.next();
			fileList.add((String) file.get("id"));
		}
		
		List<String> ret = new LinkedList<String>();
		for (String file : fileList)
		{
			res = serviceRequest(Verb.GET, "https://www.googleapis.com/drive/v2/files/" + file);
			if(!(boolean) ((JSONObject) res.get("labels")).get("trashed"))
				ret.add((String) res.get("title"));
		}
		
		String[] files = new String[ret.size()];
		ret.toArray(files);
		return files;
	}

	//works
	@SuppressWarnings({ "unchecked", "unused" })
	/**
	 * Cria uma directoria no sistema de ficheiros da google drive
	 * @param path - caminho da directoria
	 * @throws FileAlreadyExistsException - não acontece...
	 * @throws NoSuchPathException - se o caminho for inválido.
	 */
	@Override
	public void mkDir(String path) throws FileAlreadyExistsException,
			NoSuchPathException {
		
		path = parser(path);
		String folderID = "";
		Map<String, String> json = new HashMap<String, String>();
		if(!path.contains("/"))
		{
			folderID = "root";
			json.put("title", path);
		}
		else
		{
			folderID = getID(path.substring(0, path.lastIndexOf("/")));
			json.put("title", path.substring(path.lastIndexOf("/") + 1));
		}
		
		
		json.put("mimeType", "application/vnd.google-apps.folder");
		
		JSONObject obj = new JSONObject(json);
		JSONArray parents = new JSONArray();
		JSONObject parent = new JSONObject();
		parent.put("id", folderID);
		parents.add(parent);
		obj.put("parents", parents);
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://www.googleapis.com/drive/v2/files");
		request.addHeader("Host", "www.googleapis.com");
		request.addHeader("Content-Type", "application/json; charset=UTF-8");
		request.addHeader("Authorization", "Bearer " + accessToken.getToken());
		request.addBodyParameter("Content-Type", "application/json; charset=UTF-8");
		request.addPayload(obj.toJSONString());
		Response res = request.send();
//		JSONParser parser = new JSONParser();
//		try {
//			obj = (JSONObject) parser.parse(res.getBody());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		json.clear();
//		json.put("id", (String) obj.get("id"));
//		obj = new JSONObject(json);
//		
//		request = new OAuthRequest(Verb.POST, "https://www.googleapis.com/drive/v2/files/"
//				+ folderID + "/children");
//		request.addHeader("Content-Type", "application/json; charset=UTF-8");
//		request.addHeader("Authorization", "Bearer " + accessToken.getToken());
//		request.addBodyParameter("Content-Type", "application/json; charset=UTF-8");
//		request.addPayload(obj.toJSONString());
//		res = request.send();
	}

	//works
		/**
		 * Remove uma pasta para o "Lixo".
		 * @param filename - caminho da pasta.
		 * @throws NoSuchFileException se a pasta não existir ou o caminho
		 * for de um ficheiro.
		 */
	@Override
	public void rmDir(String path) throws NoSuchPathException {
		if (!isFolder(path)) {
			Logger.log(NODIR + path);
			throw new NoSuchPathException(NODIR);
		}
		
		String folderID = getID(path);
		JSONObject res = serviceRequest(Verb.GET, "https://www.googleapis.com/drive/v2/files/" +
								folderID + "/children");
		
		if(((JSONArray) res.get("items")).isEmpty())
			serviceRequest(Verb.POST,
					"https://www.googleapis.com/drive/v2/files/" + folderID + "/trash");
	}

	//works
	/**
	 * Remove um ficheiro para o "Lixo"
	 * @param filename - caminho do ficheiro.
	 * @throws NoSuchFileException se o ficheiro não existir
	 */
	@Override
	public void rm(String filename) throws NoSuchFileException {
		if (isFolder(filename))
			return;
		try {
			serviceRequest(Verb.POST,
					"https://www.googleapis.com/drive/v2/files/" + getID(filename) + "/trash");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//works
	/**
	 * @path - O caminho do ficheiro/pasta.
	 * @throws NoSuchFileException -se o caminho não for válido.
	 * @returns - os metadados do ficheiro/pasta. 
	 */
	@Override
	public FileInfo getAttr(String path) throws NoSuchFileException {
		
		try {
			String pathID = getID(path);
			JSONObject res = serviceRequest(Verb.GET, "https://www.googleapis.com/drive/v2/files/" + pathID);
			
			Date date = null;
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				date = df.parse((String) res.get("modifiedDate"));
			} catch (java.text.ParseException e1) {
				System.err.println("File Modification date parsing error.");
			}
			
			if(res.get("fileSize") != null)
			{
				return new FileInfo((String) res.get("title"),
									Long.valueOf((String) res.get("fileSize")),
									date,
									!((String) res.get("mimeType")).endsWith("folder"));
			}
			else
				return new FileInfo((String) res.get("title"),
									 0L,
									 date,
									 !((String) res.get("mimeType")).endsWith("folder"));
			
		} catch (Exception e) {
			throw new NoSuchFileException("O ficheiro não existe.");
		}
	}
	
	//works
	/**
	 * @param filename - caminho para o ficheiro na directoria remota.
	 * @throws NoSuchFileException - caminho inválido.
	 * @returns o ficheiro transferido em byte array.
	 */
	@Override
	public byte[] getFile(String filename) throws NoSuchFileException {
		
		try {
			String link = "";
			String fileID = getID(filename);
			JSONArray files = (JSONArray) serviceRequest(Verb.GET,
					  		  "https://www.googleapis.com/drive/v2/files").get("items");
			
			for(Object o: files)
				if(((JSONObject) o).get("id").equals(fileID))
					link = (String) ((JSONObject) o).get("downloadUrl");
			
			OAuthRequest request = new OAuthRequest(Verb.GET, link);
			service.signRequest(accessToken, request);
			Response response = request.send();
			
			InputStream in = response.getStream();
			int nbytes = Integer.parseInt(response.getHeader("Content-Length"));
			
			byte[] file = new byte[nbytes];
			int copied = 0;
			int fData;
			try {
				while ((fData = in.read()) > -1) {
					file[copied++] = (byte) fData;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return file;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//works
	/**
	 * Faz upload de um ficheiro com um certo caminho de destino.
	 * @param filename - caminho do ficheiro.
	 * @param fileData - bytes do ficheiro a transferir.
	 * @throws FileAlreadyExistsException - não acontece.
	 */
	@Override
	public void putFile(String filename, byte[] fileData)
			throws FileAlreadyExistsException {
		
		filename = parser(filename);
		String file = "";
		String folderID = "";
		String ID = "";
		try {
			if(filename.contains("/"))
			{
				folderID = getID(filename.substring(0, filename.lastIndexOf("/")));
				file = filename.substring(filename.lastIndexOf("/") + 1);
			}
			else
			{
				folderID = "root";
				file = filename;
			}
		} catch (NoSuchPathException e1) {
			e1.printStackTrace();
		}
		
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		
		try {
			JSONObject metadata;
			if(filename.contains("/")) {
				metadata = (JSONObject)parser.parse("{ \"title\": \""+ file +"\", \"mimeType\": \"" + (filename.substring(filename.lastIndexOf(".") + 1)) + "\", \"parents\": [ { \"id\": \"" + folderID + "\" } ] }");
			} else {
				metadata = (JSONObject)parser.parse("{ \"title\": \""+ file +"\", \"mimeType\": \"" + (filename.substring(filename.lastIndexOf(".") + 1)) + "\" }");
			}
			OAuthRequest request = new OAuthRequest(Verb.POST, "https://www.googleapis.com/drive/v2/files");
			request.addHeader("Content-Type", "application/json");
			request.addHeader("Authorization", "Bearer " + accessToken.getToken());
			request.addPayload(metadata.toJSONString());
			Response res = request.send();
			obj = (JSONObject) parser.parse(res.getBody());
			ID = (String) obj.get("id");
			request = new OAuthRequest(Verb.PUT, "https://www.googleapis.com/upload/drive/v2/files/" + ID + "?uploadType=media");
			request.addHeader("Content-Type", "application/octet-stream");
			request.addHeader("Authorization", "Bearer " + accessToken.getToken());
			request.addHeader("Content-Length", String.valueOf(fileData.length));
			request.addPayload(fileData);
			res = request.send();
			obj = (JSONObject) parser.parse(res.getBody());
		} catch(ParseException e) {
			e.printStackTrace();
		}
	}

	//works
	private boolean isFolder(String path) {
		
		try {
			String id = getID(path);
			JSONObject obj = serviceRequest(Verb.GET, "https://www.googleapis.com/drive/v2/files/" + id);
			return ((String) obj.get("mimeType")).contains("folder");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public String parser(String path)
	{
		if(path.equals(".") || path.equals("./") || path == null)
			return "";
		while(path.startsWith("./"))
			path = path.substring(2);
		return path;
	}

}
