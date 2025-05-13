package net.betaProxy.utils.jdk_fix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Properties extends Hashtable<Object,Object> {
	
	private static final long serialVersionUID = 4112578634029874840L;
	
	protected Properties defaults;
	
	public Properties() {
        this(null);
    }
	
	public Properties(Properties defaults) {
        this.defaults = defaults;
    }
	
	public synchronized void load(InputStream inStream) throws IOException {
		try(BufferedReader var1 = new BufferedReader(new InputStreamReader(inStream))) {
			String var2 = "";
		
			while(true) {
				var2 = var1.readLine();
				if(var2 == null) {
					var1.close();
					break;
				}
			
				if(!var2.startsWith("#")) {
					String[] var3 = var2.split("=");
					if(var3.length > 1) {
						put(var3[0], var3[1]);
					}
				}
			}
		}
    }
	
	public void store(OutputStream outStream, String comments) throws IOException {
		try (PrintWriter var1 = new PrintWriter(new OutputStreamWriter(outStream));) {
			var1.println("#" + comments);
			var1.println("#" + new Date().toString());
			synchronized (this) {
				List<String> list = new ArrayList<String>();
				Enumeration<?> e = keys();
				while(e.hasMoreElements()) {
					list.add((String)e.nextElement());
				}
				Collections.reverse(list);
				for (int i = 0; i < list.size(); ++i) {
					String key = list.get(i);
	                String val = (String)get(key);
	                var1.println(key + "=" + val);
				}
			}
			var1.flush();
		}
	}
	
	public synchronized Object setProperty(String key, String value) {
        return put(key, value);
    }
	
	public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }
	
	public String getProperty(String key) {
        Object oval = super.get(key);
        String sval = (oval instanceof String) ? (String)oval : null;
        return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
    }

}
