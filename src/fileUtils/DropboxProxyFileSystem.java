package fileUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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

public class DropboxProxyFileSystem implements IFileSystem {

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

	public DropboxProxyFileSystem(OAuthService service, Token accessToken) {
		super();
		this.accessToken = accessToken;
		this.service = service;
	}

	public String getPath(String path) {
		Logger.log("Transforming " + path);
		if(path.equals(".") || path.equals("./") || path == null)
			return "";
		while(path.startsWith("./"))
			path = path.substring(2);
		return path;
	}

	@Override
	public String[] ls(String path) throws NoSuchPathException {
		path = getPath(path);
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"https://api.dropbox.com/1/metadata/dropbox/" + path
						+ "?list=true");
		service.signRequest(accessToken, request);
		Response response = request.send();

		if (response.getCode() != 200) {
			switch (response.getCode()) {
			case 406:
				Logger.log(NOSUCHFILENAME + path);
				throw new NoSuchPathException("Too many files to be listed");
			default:
				Logger.log(NOSUCHFILENAME + path);
				throw new NoSuchPathException(NOSUCHFILENAME + path);
			}
		}

		JSONParser parser = new JSONParser();
		JSONObject res;
		try {
			res = (JSONObject) parser.parse(response.getBody());
			if(((boolean) res.get("is_dir")) == false)
				throw new NoSuchPathException("That is not a valid path.");
		} catch (ParseException e) {
			return null;
		}
		List<String> fileList = new LinkedList<String>();
		JSONArray items = (JSONArray) res.get("contents");

		@SuppressWarnings("rawtypes")
		Iterator it = items.iterator();
		while (it.hasNext()) {
			JSONObject file = (JSONObject) it.next();
			String filename = (String) file.get("path");
			String[] fpath = filename.split("/");
			filename = fpath[fpath.length - 1];
			fileList.add(filename);
		}
		String[] files = new String[fileList.size()];
		int counter = 0;
		for (String file : fileList) {
			files[counter++] = file;
		}
		return files;
	}

	@Override
	public void mkDir(String path) throws FileAlreadyExistsException,
			NoSuchPathException {
		path = getPath(path);
		OAuthRequest request = new OAuthRequest(Verb.POST,
				"https://api.dropbox.com/1/fileops/create_folder?root=dropbox&path="
						+ path);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println(response.getBody());
		if (response.getCode() != 200) {
			Logger.log(ALREADYEXISTSDIR + " " + path);
			throw new FileAlreadyExistsException(ALREADYEXISTSDIR);
		}
	}

	@Override
	public void rmDir(String path) throws NoSuchPathException {
		path = getPath(path);
		if (!isFolder(path)) {
			Logger.log(NODIR + path);
			throw new NoSuchPathException(NODIR);
		}
		Logger.log("Removing directory: " + path);
		OAuthRequest request = new OAuthRequest(Verb.POST,
				"https://api.dropbox.com/1/fileops/delete?root=dropbox&path="
						+ path);

		service.signRequest(accessToken, request);

		Response response = request.send();
		if (response.getCode() != 200) {
			Logger.log(NODIR + path);
			throw new NoSuchPathException(NODIR);
		}
	}

	@Override
	public void rm(String filename) throws NoSuchFileException {
		filename = getPath(filename);
		if (isFolder(filename))
			return;
		OAuthRequest request = new OAuthRequest(Verb.POST,
				"https://api.dropbox.com/1/fileops/delete?root=dropbox&path="
						+ filename);
		Logger.log("Removing file: " + filename);
		service.signRequest(accessToken, request);

		Response response = request.send();
		if (response.getCode() != 200) {
			Logger.log(NOSUCHFILENAME + filename);
			throw new NoSuchFileException(NOSUCHFILENAME + filename);
		}
	}

	@Override
	public FileInfo getAttr(String path) throws NoSuchFileException {
		path = getPath(path);
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"https://api.dropbox.com/1/metadata/dropbox/" + path);
		service.signRequest(accessToken, request);
		Response response = request.send();

		if (response.getCode() != 200) {
			Logger.log(NOSUCHFILENAME + path);
			throw new NoSuchFileException(NOSUCHFILENAME + path);
		}

		JSONParser parser = new JSONParser();
		JSONObject res;
		try {
			res = (JSONObject) parser.parse(response.getBody());
		} catch (ParseException e) {
			return null;
		}

		SimpleDateFormat df = new SimpleDateFormat(
				"EEE, d MMM yyyy HH:mm:ss Z", Locale.US);

		Date lastModified = null;
		try {
			lastModified = df.parse((String) res.get("modified"));
		} catch (java.text.ParseException e) {
		}

		long size = (long) res.get("bytes");
		FileInfo inf = new FileInfo(path, size, lastModified, !isFolder(path));
		return inf;
	}

	@Override
	public byte[] getFile(String filename) throws NoSuchFileException {
		filename = getPath(filename);
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"https://api-content.dropbox.com/1/files/dropbox/" + filename);
		service.signRequest(accessToken, request);
		Response response = request.send();

		if (response.getCode() != 200) {
			Logger.log(NOSUCHFILENAME + filename);
			throw new NoSuchFileException(NOSUCHFILENAME + filename);
		}

		Map<String, String> header = response.getHeaders();

		String data = header.get("x-dropbox-metadata");
		JSONParser parser = new JSONParser();
		JSONObject res;
		try {
			res = (JSONObject) parser.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		DataInputStream in = new DataInputStream(response.getStream());
		int nbytes = Integer.parseInt(res.get("bytes").toString());
		byte[] file = new byte[nbytes];
		try {
			in.readFully(file);
		} catch (IOException e) {
			// TODO does not happen
			e.printStackTrace();
		}

		return file;
	}

	@Override
	public void putFile(String filename, byte[] fileData)
			throws FileAlreadyExistsException {
		Logger.log(filename);
		filename = getPath(filename);
		OAuthRequest request = new OAuthRequest(Verb.PUT,
				"https://api-content.dropbox.com/1/files_put/dropbox/"
						+ filename);
		Logger.log(filename);

		request.addPayload(fileData);
		request.addHeader("Content-Type", "application/octet-stream");

		service.signRequest(accessToken, request);

		Response response = request.send();

		if (response.getCode() != 200) {
			throw new RuntimeException("Metadata response code:"
					+ response.getCode());
		}
	}

	private boolean isFolder(String path) {
		path = getPath(path);
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"https://api.dropbox.com/1/metadata/dropbox/" + path);

		service.signRequest(accessToken, request);

		Response response = request.send();
		if (response.getCode() != 200) {
			return false;
		}
		JSONParser parser = new JSONParser();
		JSONObject res;

		try {
			res = (JSONObject) parser.parse(response.getBody());
		} catch (ParseException e) {
			return false;
		}

		boolean fisFolder = (boolean) res.get("is_dir");
		return fisFolder;
	}

}
