package orioni.jz.awt.swing;

import orioni.jz.util.Pair;

import java.util.*;

/**
 * This class is designed to represent a completion context for the {@link CompletionTextField} class.  It consists of a
 * regular expression which recognizes all strings within the context, a series of strings to use as completion
 * possibilities, and a keystroke which activates the context.  Simply put, this class is a holder for that data.
 *
 * @author Zachary Palmer
 */
public class CompletionContext
{
// STATIC FIELDS /////////////////////////////////////////////////////////////////

// CONSTANTS /////////////////////////////////////////////////////////////////////

// NON-STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * The strings which should be used as completion possibilities and their corresponding display objects.
     */
    protected Set<Pair<String, Object>> completionPossibilities;
    /**
     * The key code which activates this {@link CompletionContext}.
     */
    protected int keycode;
    /**
     * The modifiers for that keycode.
     */
    protected int modifiers;

    /**
     * Determines whether or not this context is case sensitive.
     */
    protected boolean caseSensitive;

    /**
     * The root of the Trie which drives this context.
     */
    protected TrieNode trieRoot;

// CONSTRUCTORS //////////////////////////////////////////////////////////////////

    /**
     * Skeleton constructor.  Uses the possibility strings themselves as display objects.
     *
     * @param completionPossibilities The strings which can be completed.
     * @param keycode                  The keycode to activate this completion context.
     * @param modifiers                The modifiers on that keystroke which must be present (such as {@link
     *                                 java.awt.event.KeyEvent#CTRL_MASK}).
     * @param caseSensitive           Whether or not this context is case sensitive.
     */
    public CompletionContext(String[] completionPossibilities, int keycode, int modifiers, boolean caseSensitive)
    {
        this(getPairSetForArray(completionPossibilities), keycode, modifiers, caseSensitive);
    }

    /**
     * General constructor.
     *
     * @param completionPossibilities A pairing of strings which can be completed and their display objects.
     * @param keycode                  The keycode to activate this completion context.
     * @param modifiers                The modifiers on that keystroke which must be present (such as {@link
     *                                 java.awt.event.KeyEvent#CTRL_MASK}).
     * @param caseSensitive           Whether or not this context is case sensitive.
     */
    public CompletionContext(Set<Pair<String, Object>> completionPossibilities, int keycode, int modifiers,
                             boolean caseSensitive)
    {
        this.completionPossibilities = completionPossibilities;
        this.keycode = keycode;
        this.modifiers = modifiers;
        this.caseSensitive = caseSensitive;

        trieRoot = new TrieNode();

        if (this.caseSensitive)
        {
            for (Pair<String, Object> pairing : this.completionPossibilities)
            {
                trieRoot.add(pairing);
            }
        } else
        {
            for (Pair<String, Object> pairing : this.completionPossibilities)
            {
                trieRoot.add(pairing, pairing.getFirst().toLowerCase());
            }
        }
    }

// NON-STATIC METHODS ////////////////////////////////////////////////////////////

    public Set<Pair<String, Object>> getCompletionPossibilities()
    {
        return Collections.unmodifiableSet(completionPossibilities);
    }

    public int getKeycode()
    {
        return keycode;
    }

    public int getModifiers()
    {
        return modifiers;
    }

    /**
     * Determines whether or not this context can complete the provided prefix string.
     *
     * @param string The prefix string.
     * @return <code>true</code> if this context can complete the provided string; <code>false</code> otherwise.
     */
    public boolean canComplete(String string)
    {
        if (!caseSensitive) string = string.toLowerCase();
        return (trieRoot.follow(string) != null);
    }

    /**
     * Retrieves all of the string/display-object pairings in which the string is prefixed by the specified string.
     *
     * @param string The prefixing string.
     * @return A set of possible completion options.
     */
    public Set<Pair<String, Object>> getPossibilitiesFor(String string)
    {
        if (!caseSensitive) string = string.toLowerCase();
        TrieNode node = trieRoot.follow(string);
        if (node == null)
        {
            return Collections.unmodifiableSet(new HashSet<Pair<String, Object>>());
        } else
        {
            return node.getDeepContents();
        }
    }

// STATIC METHODS ////////////////////////////////////////////////////////////////

    /**
     * Performs the service of transforming a {@link Set}<code>&lt;{@link String}&gt; into a {@link Map}<code>&lt;{@link
     * String}, {@link Object}</code> by mapping all of the values to themselves.
     *
     * @param set The {@link Set} to transform.
     * @return The resulting map.
     */
    private static Set<Pair<String, Object>> getPairSetForArray(String[] set)
    {
        Set<Pair<String, Object>> ret = new HashSet<Pair<String, Object>>();
        for (String s : set)
        {
            ret.add(new Pair<String, Object>(s, s));
        }
        return ret;
    }

// CONTAINED CLASSES /////////////////////////////////////////////////////////////

    /**
     * This class is used to form the basis of the Trie structure which determines whether or not a string is
     * completable.  This Trie is a tree structure in which each character of a string leads to a child node.  At the
     * end of a path, the contents of that node include the appropriate pairing.  For example, if one started at the
     * root of the tree and followed the path 'd', 'o', and 'g', one would find one or more of three things: <ol>
     * <li>That, at the node in question, its contents contain a pairing which is between the string "dog" and a display
     * object, indicating that "dog" is syntactically valid.</li> <li>That, at the node in question, children exist,
     * indicating that a string with the prefix "dog" is syntactically valid.</li> <li>That, at some point, no child
     * associated with the appropriate character exists, breaking the path and indicating that no string either equal to
     * or with the prefix "dog" is syntactically valid.</li> </ol>
     *
     * @author Zachary Palmer
     */
    static class TrieNode
    {
        /**
         * The mapping between characters of this point and the trie and the nodes to which they lead.
         */
        protected Map<Character, TrieNode> children;
        /**
         * The {@link Pair}ings of items which exist at this node.
         */
        protected Set<Pair<String, Object>> contents;

        /**
         * General constructor.
         */
        public TrieNode()
        {
            children = new HashMap<Character, TrieNode>();
            contents = new HashSet<Pair<String, Object>>();
        }

        /**
         * Retrieves the contents of this node.
         *
         * @return The {@link Pair}ings which exist at this node.
         */
        public Set<Pair<String, Object>> getContents()
        {
            return Collections.unmodifiableSet(contents);
        }

        /**
         * Retrieves the contents of this node and all of its children.
         *
         * @return The {@link Pair}ings which exist at this node and all nodes below it.
         */
        public Set<Pair<String, Object>> getDeepContents()
        {
            HashSet<Pair<String, Object>> ret = new HashSet<Pair<String, Object>>();
            ret.addAll(getContents());
            for (TrieNode node : children.values())
            {
                ret.addAll(node.getDeepContents());
            }
            return Collections.unmodifiableSet(ret);
        }

        /**
         * Adds an item to the contents of trie structure.
         *
         * @param pairing The pairing to add.
         */
        public void add(Pair<String, Object> pairing)
        {
            add(pairing, pairing.getFirst());
        }

        /**
         * Adds the provided item to this trie structure.  The provided name is used at the root of this node; its first
         * character corresponds directly to this node's child mapping.  If that name is blank, the item is added to the
         * contents of this node.  Otherwise, a child node is created if necessary and receives this call excluding the
         * first character, such that the child node may pass the item in question down the trie.
         *
         * @param pairing The pairing to add.
         * @param name    The name of that pairing at this node.
         */
        public void add(Pair<String, Object> pairing, String name)
        {
            if (name.length() == 0)
            {
                contents.add(pairing);
            } else
            {
                TrieNode child = children.get(name.charAt(0));
                if (child == null)
                {
                    child = new TrieNode();
                    children.put(name.charAt(0), child);
                }
                child.add(pairing, name.substring(1));
            }
        }

        /**
         * Retrieves the child node associated with the provided character.
         *
         * @param c The character in question.
         * @return The child node associated with that character, or <code>null</code> if no such node exists.
         */
        public TrieNode getChild(Character c)
        {
            return children.get(c);
        }

        /**
         * Follows the path specified by the provided string.  The node at that location is returned.
         *
         * @param string The string path to follow.
         * @return The node at that location, or <code>null</code> if no node exists at that location.
         */
        public TrieNode follow(String string)
        {
            TrieNode node = this;
            for (char c : string.toCharArray())
            {
                node = node.getChild(c);
                if (node == null) return null;
            }
            return node;
        }
    }
}

// END OF FILE