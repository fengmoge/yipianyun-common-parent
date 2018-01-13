package yipianyun.common.utils;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.PartBase;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.util.EncodingUtil;

public class DesFilePart extends PartBase {

	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String DEFAULT_TRANSFER_ENCODING = "binary";
	protected static final String FILE_NAME = "; filename=";
	private static final byte[] FILE_NAME_BYTES = EncodingUtil.getBytes(
			"; filename=", "UTF-8");
	private PartSource source;

	public DesFilePart(String name, PartSource partSource, String contentType,
			String charset) {
		super(name, contentType == null ? "application/octet-stream"
				: contentType, charset == null ? "ISO-8859-1" : charset,
				"binary");

		if (partSource == null) {
			throw new IllegalArgumentException("Source may not be null");
		}
		this.source = partSource;
	}

	public DesFilePart(String name, PartSource partSource) {
		this(name, partSource, null, null);
	}

	public DesFilePart(String name, File file) throws FileNotFoundException {
		this(name, new FilePartSource(file), null, null);
	}

	public DesFilePart(String name, File file, String contentType,
			String charset) throws FileNotFoundException {
		this(name, new FilePartSource(file), contentType, charset);
	}

	public DesFilePart(String name, String fileName, File file)
			throws FileNotFoundException {
		this(name, new FilePartSource(fileName, file), null, null);
	}

	public DesFilePart(String name, String fileName, File file,
			String contentType, String charset) throws FileNotFoundException {
		this(name, new FilePartSource(fileName, file), contentType, charset);
	}

	protected void sendDispositionHeader(OutputStream out) throws IOException {
		super.sendDispositionHeader(out);
		String filename = this.source.getFileName();
		if (filename != null) {
			out.write(FILE_NAME_BYTES);
			out.write(QUOTE_BYTES);
			out.write(EncodingUtil.getBytes(filename, "UTF-8"));
			out.write(QUOTE_BYTES);
		}
	}

	protected void sendData(OutputStream out) throws IOException {
		if (lengthOfData() == 0L) {
			return;
		}

		byte[] tmp = new byte[4096];
		InputStream instream = this.source.createInputStream();
		try {
			int len;
			while ((len = instream.read(tmp)) >= 0)
				out.write(tmp, 0, len);
		} finally {
			instream.close();
		}
	}

	protected PartSource getSource() {
		return this.source;
	}

	protected long lengthOfData() throws IOException {
		return this.source.getLength();
	}

}
