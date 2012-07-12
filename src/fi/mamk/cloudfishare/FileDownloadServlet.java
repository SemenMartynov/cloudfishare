package fi.mamk.cloudfishare;

import com.google.appengine.api.datastore.Blob;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FileDownloadServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(FileDownloadServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse res)	throws ServletException, IOException {
		try {
			// find desired image
		    PersistenceManager pm = PMF.get().getPersistenceManager();
		    try { 	
		    	
		    	Query query = pm.newQuery("select from "+ BinObject.class.getName() +
			            " where code == codeParam " +
			            "parameters String codeParam");
		    	List<BinObject> results = (List<BinObject>)query.execute(req.getParameter("code"));
		    	Blob binObject = results.iterator().next().getContent();

		    	// serve the first image
		    	log.warning("Your file is " + results.iterator().next().getName());
		    	res.setContentType("application/octet-stream");
		    	res.setHeader("Content-Disposition", "attachment; filename=" + results.iterator().next().getName());
		    	res.getOutputStream().write(binObject.getBytes());
		    } catch (Exception NoSuchElementException) {
		    	log.warning("File not found");
		    	res.setContentType("text/plain");
			    res.getWriter().println("File not exist or you input wrong code");
		    } finally {
		    	pm.close();
		    }
			
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}