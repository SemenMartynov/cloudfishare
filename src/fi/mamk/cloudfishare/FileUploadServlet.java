package fi.mamk.cloudfishare;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;

import java.io.InputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FileUploadServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(FileUploadServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {
		try {
			ServletFileUpload upload = new ServletFileUpload();
			//res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					log.warning("Got a form field: " + item.getFieldName());
				} else {
					log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName());
				
					// generating code
					SessionIdentifierGenerator generator = new SessionIdentifierGenerator();
					String code = generator.nextSessionId();
					
					// construct our entity objects
				    Blob myBlob = new Blob(IOUtils.toByteArray(stream));
				    BinObject binObject = new BinObject(item.getName(), code, myBlob);

				    // persist image
				    PersistenceManager pm = PMF.get().getPersistenceManager();
				    try {
				    	pm.makePersistent(binObject);
				    	
					    // respond to query
					    res.setContentType("text/plain");
					    res.getWriter().println("OK! Your code is " + code);
				    } finally {
				    	pm.close();
				    }
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}