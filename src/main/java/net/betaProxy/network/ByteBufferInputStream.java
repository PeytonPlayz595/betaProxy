package net.betaProxy.network;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
	
	private final ByteBuffer buffer;
	private int readLimitNow = 0;
	
	public ByteBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int read() throws IOException {
		if(buffer.remaining() > 0) {
			--readLimitNow;
			return ((int)buffer.get() & 0xFF);
		}else {
			return -1;
		}
	}
	
	public long skip(long n) throws IOException {
		int l = buffer.remaining();
		if(n > l) {
			n = l;
		}
		buffer.position(buffer.position() + (int)n);
		readLimitNow -= n;
		return n;
	}
	
	public int read(byte b[], int off, int len) throws IOException {
		int l = buffer.remaining();
		if(len > l) {
			len = l;
		}
		buffer.get(b, off, len);
		readLimitNow -= len;
		return len;
	}
	
	public int available() throws IOException {
		return buffer.remaining();
	}
	
	public synchronized void mark(int readlimit) {
		buffer.mark();
		readLimitNow = readlimit;
	}
	
	public synchronized void reset() throws IOException {
		if(readLimitNow <= 0) {
			throw new IOException("Mark does not exist");
		}
		buffer.reset();
	}
	
	public boolean markSupported() {
		return true;
	}
	
}