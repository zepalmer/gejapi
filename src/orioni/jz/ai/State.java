package orioni.jz.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents the general concept of a {@link State} in an exhaustive or semi-exhaustive state search.
 *
 * @author Zachary Palmer
 */
public abstract class State implements Comparable
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * General constructor.
     */
    public State()
    {
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * This method produces all new {@link State} objects which are legal from this {@link State}.
     *
     * @param history A {@link Set} of {@link State} objects which have previously been visited and should not be
     *                returned.
     * @return A {@link State}<code>[]</code> containing all legal moves from this {@link State}.
     */
    public abstract State[] getStates(Set history);

    /**
     * This method evaluates whether or not this {@link State} is a "winning" state.
     *
     * @return <code>true</code> if this state is a "win"; <code>false</code> otherwise.
     */
    public abstract boolean isWin();

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * This method performs a simple linear breadth-first state search on the provided initial state set.  If a solution
     * is found, the first solution found is returned; if not, then <code>null</code> is returned.
     *
     * @param initialStates A {@link Set} containing the initial states for the problem.
     * @return The {@link State} which represents the first solution found, or <code>null</code> if no solution was
     *         found.
     */
    public static State performBreadthFirstSearch(Set<State> initialStates)
    {
        Set<State> history = new HashSet<State>();
        List<State> queue = new ArrayList<State>();
        for (Object initialState : initialStates)
        {
            State state = (State) (initialState);
            if (state.isWin()) return state;
            queue.add(state);
            history.add(state);
        }
        while (queue.size() > 0)
        {
            State currentState = queue.remove(0);
            State[] newStates = currentState.getStates(history);
            for (State state : newStates)
            {
                if (state.isWin()) return state;
                queue.add(state);
            }
        }
        return null;
    }
}