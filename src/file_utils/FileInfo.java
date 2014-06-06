package file_utils;
import java.io.Serializable;
import java.util.*;

public class FileInfo implements Serializable {
	private static final long serialVersionUID = -4498079336259690561L;
	public String name;
	public long length;
	public Date modified;
	public boolean isFile;
	
	public FileInfo(String name, long length, Date modified, boolean isFile) {
		this.name = name;
		this.length = length;
		this.modified = modified;
		this.isFile = isFile;
	}
	
	public String toString() {
		return "Name : " + name + "\nLength: " + length + "\nData modified: " + modified + "\nisFile : " + isFile; 
	}
}
