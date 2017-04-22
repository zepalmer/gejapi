package orioni.jz.io.files.abstractnode;

import java.util.Collections;
import java.util.Set;

/**
 * This type of exception is thrown whenever a method is incapable of determining which protocol should be used to
 * perform a certain action.  This may be the case if more than one protocol volunteers its services and the appropriate
 * protocol cannot be determined or if no protocol can be found to perform the task.
 *
 * @author Zachary Palmer
 */
public class ProtocolAmbiguityException extends Exception
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The protocols which are candidates for selection.
     */
    protected Set<AbstractFilesystemProtocol> protocols;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.
     *
     * @param protocols The {@link orioni.jz.io.files.abstractnode.AbstractFilesystemProtocol}s which could have been
     *                  used to perform the specified action.
     */
    public ProtocolAmbiguityException(Set<AbstractFilesystemProtocol> protocols)
    {
        super();
        this.protocols = protocols;
    }

    /**
     * General constructor.
     *
     * @param message   A message which further describes the reason the exception was thrown.
     * @param protocols The {@link orioni.jz.io.files.abstractnode.AbstractFilesystemProtocol}s which could have been
     *                  used to perform the specified action.
     */
    public ProtocolAmbiguityException(String message, Set<AbstractFilesystemProtocol> protocols)
    {
        super(message);
        this.protocols = Collections.unmodifiableSet(protocols);
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * Retrieves the candidate protocols which could serve as the protocol to perform the task which threw the {@link
     * orioni.jz.io.files.abstractnode.ProtocolAmbiguityException}.
     *
     * @return The {@link java.util.Set} containing all of the protocols which could be used to perform the task.  This
     *         set is immutable.
     */
    public Set<AbstractFilesystemProtocol> getCandidateProtocols()
    {
        return protocols;
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

}