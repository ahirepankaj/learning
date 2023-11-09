import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNReportGenerator {
    public static void main(String[] args) {
        String svnUrl = "https://example.com/svn/repo";
        String username = "your_username";
        String password = "your_password";

        try {
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnUrl));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
            repository.setAuthenticationManager(authManager);

            // List repositories
            Collection<SVNDirEntry> repositories = repository.getDir("", -1, null, (Collection<SVNDirEntry>) null);
            for (SVNDirEntry repositoryEntry : repositories) {
                System.out.println("Repository: " + repositoryEntry.getName());

                // List branches
                Collection<SVNDirEntry> branches = repository.getDir(repositoryEntry.getName() + "/branches", -1, null, (Collection<SVNDirEntry>) null);
                for (SVNDirEntry branchEntry : branches) {
                    System.out.println("  Branch: " + branchEntry.getName());
                    // Retrieve branch details
                    SVNProperties properties = repository.getFile("", branchEntry.getRevision(), null, null);
                    String author = properties.getStringValue("svn:author");
                    Date creationDate = properties.getDateValue(SVNProperty.COMMITTED_DATE);
                    Date lastUpdateDate = properties.getDateValue(SVNProperty.LAST_AUTHOR_DATE);
                    
                    System.out.println("    Author: " + author);
                    System.out.println("    Creation Date: " + creationDate);
                    System.out.println("    Last Update Date: " + lastUpdateDate);
                }
            }
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }
}
