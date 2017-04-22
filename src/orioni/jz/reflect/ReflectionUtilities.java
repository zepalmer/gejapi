package orioni.jz.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This contained static class in the <code>Utilities</code> class provides support for Java reflection and related
 * activities.
 *
 * @author Zachary Palmer
 */
public class ReflectionUtilities
{

// STATIC FIELDS /////////////////////////////////////////////////////////////

    /**
     * Maintains a Method[] cache, mapping Method search queries to their results.  This allows the reflection calls to
     * be executed much faster.
     */
    protected static Map<MethodQueryCacheKey, Method[]> methodQueryCache;
    /**
     * Acts as a prepared key for the <code>findMatchingMethods</code> method to prevent needless Object creation. As a
     * result of the globalization of this static field, however, the <code>findMatchingMethod</code> method is
     * synchronized.
     */
    protected static MethodQueryCacheKey methodQueryCacheKey;

    /**
     * Maintains a Method[] cache for the <code>getMethods</code> method, mapping results of the calls to
     * <code>Class.getMethods()</code> method to their results.
     */
    protected static Map<Class, Method[]> getMethodsCache;

    /**
     * Maintains a Constructor[] cache, mapping Constructor search queries to their results.  This allows the reflection
     * calls to be executed much faster.
     */
    protected static Map<ParameterSetCacheKey, Constructor[]> constructorQueryCache;
    /**
     * Acts as a prepared key for the <code>findMatchingConstructors</code> method to prevent needless Object creation.
     * As a result of the globalization of this static field, however, the <code>findMatchingConstructor</code> method
     * is synchronized.
     */
    protected static ParameterSetCacheKey constructorQueryCacheKey;

    /**
     * Maintains a Constructor[] cache for the <code>getConstructors</code> method, mapping results of the calls to
     * <code>Class.getConstructors()</code> method to their results.
     */
    protected static Map<Class, Constructor[]> getConstructorsCache;

// CONSTRUCTORS //////////////////////////////////////////////////////////////

    /**
     * Prevents <code>ReflectionUtilities</code> from being instantiated.
     */
    private ReflectionUtilities()
    {
    }

    /**
     * Static initialization.
     */
    static
    {
        methodQueryCache = new HashMap<MethodQueryCacheKey, Method[]>();
        methodQueryCacheKey = new MethodQueryCacheKey();
        getMethodsCache = new HashMap<Class, Method[]>();
        constructorQueryCache = new HashMap<ParameterSetCacheKey, Constructor[]>();
        constructorQueryCacheKey = new ParameterSetCacheKey();
        getConstructorsCache = new HashMap<Class, Constructor[]>();
    }

// STATIC METHODS ////////////////////////////////////////////////////////////

    /**
     * An advanced form of <code>instanceof</code>; determines whether or not the given Object is the type of object
     * represented by the given Class.
     *
     * @param obj        The Object to test.
     * @param superClass The class against which to test.
     */
    public static boolean instanceOf(Object obj, Class superClass)
    {
        Class testClass = obj.getClass();
        return subclassOf(testClass, superClass);
    }

    /**
     * Core of <code>instanceOf</code>.  Determines whether or not a class is a subclass or implementer of another class
     * or if it is equal to the other class.
     *
     * @param testClass  The class being tested.
     * @param superClass The prospective superclass or implementee.
     * @return <code>true</code> if the test class and super class are the same or if the test class represents a
     *         subclass of the super class.
     */
    public static boolean subclassOf(Class testClass, Class superClass)
    {
        if (testClass == null) return false;
        if (testClass.equals(superClass)) return true;
        if (superClass.isInterface())
        {
            while (testClass != null)
            {
                if (implementerOf(testClass, superClass))
                {
                    return true;
                }
                testClass = testClass.getSuperclass();
            }
        } else
        {
            // The superclass is a normal class.  Easy check!
            while (testClass != null)
            {
                if (testClass.equals(superClass))
                {
                    return true;
                }
                testClass = testClass.getSuperclass();
            }
        }
        return false;
    }

    /**
     * Determines whether or not the given test class implements the interface represented by the interface class.  This
     * method is mainly used by <code>instanceOf</code>.
     *
     * @param testClass      The class to check for implementation.
     * @param interfaceClass The interface for which to check.
     */
    public static boolean implementerOf(Class testClass, Class interfaceClass)
    {
        if (testClass.equals(interfaceClass)) return true;
        Class[] subinterfaces = testClass.getInterfaces();
        for (final Class subinterface : subinterfaces)
        {
            if (implementerOf(subinterface, interfaceClass))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds all methods of the given Class.  This method is different from <code>Class.getMethods</code> only in that
     * it caches the results of the call.  This is done because calls to <code>Class.getMethods</code> are slow and,
     * barring post-VM-start class loading, repeatable. <P> To clear the caches used by this method, call
     * <code>clearMethodCaches</code>.
     *
     * @param cl The Class for which all Methods should be retrieved.
     */
    public static Method[] getMethods(Class cl)
    {
        // The following call is safe because the values in the table will either be Method[] or null.
        Method[] ret = getMethodsCache.get(cl);

        // On a cache hit, return results.
        if (ret != null) return ret;

        // There's a first time for everything.  ;)
        ret = cl.getMethods();
        getMethodsCache.put(cl, ret);
        return ret;
    }

    /**
     * Finds all methods of the given Class with the given name that can use the provided parameters.  This method will
     * consider a comparison between a variable in <code>params</code> of an immutable primitive representation (i.e.,
     * <code>Integer</code>, <code>Boolean</code>, etc.) and a class value of <code>cl</code> of that primitive's class
     * (i.e., <code>int.class</code> and <code>boolean.class</code> respectively) to be a match.  This allows the
     * <code>findMethods</code> method to locate methods based on parameters while still including methods requiring
     * primitive-type parameters. <P> Because the results of the <code>findMethods</code> method are predictable
     * (barring any post-VM-start class loading) and because Java reflection tends to take its time in determining the
     * result of reflection calls, this method caches its results in a <code>Hashtable</code> based on the types of
     * parameters provided.  A cache is also used on the <code>Method[]</code> results of the
     * <code>Class.getMethods()</code> method. <P> To clear the caches used by this method, call
     * <code>clearMethodCaches</code>. <P> Because the caching mechanism is tested by a global key to prevent needless
     * object creation, this method is synchronized.
     *
     * @param cl     The Class to search for matching methods.
     * @param name   The name of the Method for which to search.
     * @param params The parameters that must be executable on a method for it to be returned.
     * @return An array of Method objects which represents the Methods which match that query.  If no methods match the
     *         query, the value returned is a <code>Method[0]</code>.
     */
    public synchronized static Method[] findMethods(Class cl, String name, Object[] params)
    {
        int i;

        // Check the method cache for a match
        methodQueryCacheKey.setKeyValues(cl, name, params);
        Object value = methodQueryCache.get(methodQueryCacheKey);
        if (value instanceof Method[])
        {
            // A positive result was found.
            return (Method[]) (value);
        }

        // No result was found.  We'll have to fetch it ourselves.

        Method[] all = getMethods(cl);
        List<Method> list = new ArrayList<Method>();

        // Analyze possible matches
        for (i = 0; i < all.length; i++)
        {
            if ((all[i].getName().equals(name)) && (params.length == all[i].getParameterTypes().length))
            {
                Class[] methodParamTypes = all[i].getParameterTypes();
                boolean add = true;
                for (int j = 0; j < methodParamTypes.length; j++)
                {
                    // If this parameter does not match the provided requirements of the method, do not return it.
                    if (params[j] == null)
                    {
                        // Null is legal as long as the type is not a primitive.
                        if ((methodParamTypes[j].equals(boolean.class)) ||
                            (methodParamTypes[j].equals(byte.class)) ||
                            (methodParamTypes[j].equals(char.class)) ||
                            (methodParamTypes[j].equals(short.class)) ||
                            (methodParamTypes[j].equals(int.class)) ||
                            (methodParamTypes[j].equals(long.class)) ||
                            (methodParamTypes[j].equals(float.class)) ||
                            (methodParamTypes[j].equals(double.class)))
                        {
                            add = false;
                            break;
                        }
                    } else
                    {
                        // Use instanceOf to determine if the parameter is acceptable.  Remember to account for
                        // reflection's need for Java immutable primitive classes in the place of actual primitives
                        // in Method.invoke calls.

                        // I'd personally much prefer if a mutable version were acceptable.
                        if ((!instanceOf(params[j], methodParamTypes[j])) &&
                            (!
                                    (((methodParamTypes[j].equals(boolean.class)) && (params[j] instanceof Boolean)) ||
                                     ((params[j] instanceof Number) &&
                                      // This speeds up the check if param is not a Number
                                      (((methodParamTypes[j].equals(byte.class)) && (params[j] instanceof Byte)) ||
                                       ((methodParamTypes[j].equals(char.class)) && (params[j] instanceof Character)) ||
                                       ((methodParamTypes[j].equals(short.class)) && (params[j] instanceof Short)) ||
                                       ((methodParamTypes[j].equals(int.class)) && (params[j] instanceof Integer)) ||
                                       ((methodParamTypes[j].equals(long.class)) && (params[j] instanceof Long)) ||
                                       ((methodParamTypes[j].equals(float.class)) && (params[j] instanceof Float)) ||
                                       ((methodParamTypes[j].equals(double.class)) &&
                                        (params[j] instanceof Double)))))))
                        {
                            add = false;
                            break;
                        }
                    }
                }
                if (add)
                {
                    list.add(all[i]);
                }
            }
        }

        // Establish results
        Method[] ret = list.toArray(new Method[0]);

        // Cache results, turning the key object over to the hashtable.
        methodQueryCache.put(methodQueryCacheKey, ret);

        // Return results
        return ret;
    }

    /**
     * Finds all constructors of the given Class.  This method is different from <code>Class.getConstructors</code> only
     * in that it caches the results of the call.  This is done because calls to <code>Class.getConstructors</code> are
     * slow and, barring post-VM-start class loading, repeatable. <P> To clear the caches used by this method, call
     * <code>clearConstructorCaches</code>.
     *
     * @param cl The Class for which all Constructors should be retrieved.
     */
    public static Constructor[] getConstructors(Class cl)
    {
        // The following call is safe because the values in the table will either be Method[] or null.
        Constructor[] ret = getConstructorsCache.get(cl);

        // On a cache hit, return results.
        if (ret != null) return ret;

        // There's a first time for everything.  ;)
        ret = cl.getConstructors();
        getConstructorsCache.put(cl, ret);
        return ret;
    }

    /**
     * Finds all constructors of the given Class with the given name that can use the provided parameters.  This method
     * will consider a comparison between a variable in <code>params</code> of an immutable primitive representation
     * (i.e., <code>Integer</code>, <code>Boolean</code>, etc.) and a class value of <code>cl</code> of that primitive's
     * class (i.e., <code>int.class</code> and <code>boolean.class</code> respectively) to be a match.  This allows the
     * <code>findConstructors</code> method to locate methods based on parameters while still including methods
     * requiring primitive-type parameters. <P> Because the results of the <code>findConstructors</code> method are
     * repeatable (barring any post-VM-start class loading) and because Java reflection tends to take its time in
     * determining the result of reflection calls, this method caches its results in a <code>Hashtable</code> based on
     * the types of parameters provided.  A cache is also used on the <code>Constructor[]</code> results of the
     * <code>Class.getConstructors()</code> method. <P> To clear the caches used by this method, call
     * <code>clearConstructorCaches</code>. <P> Because the caching mechanism is tested by a global key to prevent
     * needless object creation, this method is synchronized.
     *
     * @param cl     The Class to search for matching methods.
     * @param params The parameters that must be executable on a method for it to be returned.
     * @return An array of Constructor objects which represents the Methods which match that query.  If no methods match
     *         the query, the value returned is a <code>Constructor[0]</code>.
     */
    public synchronized static Constructor[] findConstructors(Class cl, Object[] params)
    {
        int i;

        // Check the method cache for a match
        constructorQueryCacheKey.setKeyValues(cl, params);
        Object value = constructorQueryCache.get(constructorQueryCacheKey);
        if (value instanceof Constructor[])
        {
            // A positive result was found.
            return (Constructor[]) (value);
        }

        // No result was found.  We'll have to fetch it ourselves.

        Constructor[] all = getConstructors(cl);
        List<Constructor> list = new ArrayList<Constructor>();

        // Analyze possible matches
        for (i = 0; i < all.length; i++)
        {
            if (params.length == all[i].getParameterTypes().length)
            {
                Class[] constructorParamTypes = all[i].getParameterTypes();
                boolean add = true;
                for (int j = 0; j < constructorParamTypes.length; j++)
                {
                    // If this parameter does not match the provided requirements of the constructor, do not return it.
                    if (params[j] == null)
                    {
                        // Null is legal as long as the type is not a primitive.
                        if ((constructorParamTypes[j].equals(boolean.class)) ||
                            (constructorParamTypes[j].equals(byte.class)) ||
                            (constructorParamTypes[j].equals(char.class)) ||
                            (constructorParamTypes[j].equals(short.class)) ||
                            (constructorParamTypes[j].equals(int.class)) ||
                            (constructorParamTypes[j].equals(long.class)) ||
                            (constructorParamTypes[j].equals(float.class)) ||
                            (constructorParamTypes[j].equals(double.class)))
                        {
                            add = false;
                            break;
                        }
                    } else
                    {
                        // Use instanceOf to determine if the parameter is acceptable.  Remember to account for
                        // reflection's need for Java immutable primitive classes in the place of actual primitives
                        // in Constructor.newInstance calls.

                        // I'd personally much prefer if a mutable version were acceptable.
                        if ((!instanceOf(params[j], constructorParamTypes[j])) &&
                            (!
                                    (((constructorParamTypes[j].equals(boolean.class)) &&
                                      (params[j] instanceof Boolean)) ||
                                                                      ((params[j] instanceof Number) &&
                                                                       // This speeds up the check if param is not a Number
                                                                       (((constructorParamTypes[j].equals(
                                                                               byte.class)) &&
                                                                                            (params[j] instanceof Byte)) ||
                                                                                                                         ((constructorParamTypes[j].equals(
                                                                                                                                 char.class)) &&
                                                                                                                                              (params[j] instanceof Character)) ||
                                                                                                                                                                                ((constructorParamTypes[j].equals(
                                                                                                                                                                                        short.class)) &&
                                                                                                                                                                                                      (params[j] instanceof Short)) ||
                                                                                                                                                                                                                                    ((constructorParamTypes[j].equals(
                                                                                                                                                                                                                                            int.class)) &&
                                                                                                                                                                                                                                                        (params[j] instanceof Integer)) ||
                                                                                                                                                                                                                                                                                        ((constructorParamTypes[j].equals(
                                                                                                                                                                                                                                                                                                long.class)) &&
                                                                                                                                                                                                                                                                                                             (params[j] instanceof Long)) ||
                                                                                                                                                                                                                                                                                                                                          ((constructorParamTypes[j].equals(
                                                                                                                                                                                                                                                                                                                                                  float.class)) &&
                                                                                                                                                                                                                                                                                                                                                                (params[j] instanceof Float)) ||
                                                                                                                                                                                                                                                                                                                                                                                              ((constructorParamTypes[j].equals(
                                                                                                                                                                                                                                                                                                                                                                                                      double.class)) &&
                                                                                                                                                                                                                                                                                                                                                                                                                     (params[j] instanceof Double)))))))
                        {
                            add = false;
                            break;
                        }
                    }
                }
                if (add)
                {
                    list.add(all[i]);
                }
            }
        }

        // Establish results
        Constructor[] ret = list.toArray(new Constructor[0]);

        // Cache results, turning the key object over to the hashtable.
        constructorQueryCache.put(constructorQueryCacheKey, ret);

        // Return results
        return ret;
    }

// CONTAINED CLASSES /////////////////////////////////////////////////////

    /**
     * This class serves as a key for the constructor query cache.  It returns an <code>equals</code> result of
     * <code>true</code> if the type of object and types of parameters provided by the other key object are identical to
     * those contained within.
     *
     * @author Zachary Palmer
     */
    public static class ParameterSetCacheKey
    {
        /**
         * The Class of the Object on which the method should be found.
         */
        protected Class objectType;
        /**
         * The Classes of the parameters that the method will require.
         */
        protected Class[] parameterTypes;

        /**
         * The hashcode for this key object.
         */
        protected int hashcode;

        /**
         * General constructor.
         */
        public ParameterSetCacheKey()
        {
            hashcode = 0;
        }

        /**
         * Sets the values of this cache key.
         *
         * @param cl     The Class on which the method is to be found.
         * @param params An Object[] containing the parameters of the method to find.
         */
        public void setKeyValues(Class cl, Object[] params)
        {
            // Build the hashcode as the values are set (to ensure one set of loop iterations)
            objectType = cl;
            hashcode = objectType.hashCode();
            parameterTypes = new Class[params.length];
            for (int i = 0; i < parameterTypes.length; i++)
            {
                parameterTypes[i] = params[i].getClass();
                hashcode ^= parameterTypes[i].hashCode();
            }
        }

        /**
         * Retrieves the Class of the Object on which the Method that this object keys is found.
         *
         * @return The Class of the Object on which the Method that this object keys is found.
         */
        public Class getObjectType()
        {
            return objectType;
        }

        /**
         * Retrieves an array of Class objects which represents the types of parameters that the Method that this object
         * keys will expect.
         *
         * @return An array of Class objects representing parameter types.
         */
        public Class[] getParameterTypes()
        {
            return parameterTypes;
        }

        /**
         * Returns the hashcode for this key.
         *
         * @return The hashcode for this key.
         */
        public int hashCode()
        {
            return hashcode;
        }

        /**
         * Determine whether or not this key object represents the same key as another object.
         *
         * @return <code>true</code> if the other object is a <code>MethodQueryCacheKey</code> with the same content as
         *         this one.
         */
        public boolean equals(Object o)
        {
            try
            {
                ParameterSetCacheKey otherKey = (ParameterSetCacheKey) o;
                Class otherObjType = otherKey.getObjectType();
                Class[] otherParamTypes = otherKey.getParameterTypes();
                if (!(otherObjType.equals(objectType))) return false;
                if (otherParamTypes.length != parameterTypes.length) return false;
                for (int i = 0; i < otherParamTypes.length; i++)
                {
                    if (!(otherParamTypes[i].equals(parameterTypes[i]))) return false;
                }
                return true;
            } catch (ClassCastException e)
            {
                return false;
            }
        }

    }

    /**
     * This class serves as a key for the method query cache.  It returns an <code>equals</code> result of
     * <code>true</code> if the type of object, types of parameters, and method name provided by the other key object
     * are identical to those contained within.
     *
     * @author Zachary Palmer
     */
    public static class MethodQueryCacheKey extends ParameterSetCacheKey
    {
        /**
         * The name of the method to find.
         */
        protected String methodName;

        /**
         * General constructor.
         */
        public MethodQueryCacheKey()
        {
            hashcode = 0;
        }

        /**
         * Sets the values of this cache key.
         *
         * @param cl     The Class on which the method is to be found.
         * @param name   The name of the method to find.
         * @param params An Object[] containing the parameters of the method to find.
         */
        public void setKeyValues(Class cl, String name, Object[] params)
        {
            super.setKeyValues(cl, params);
            methodName = name;
            hashcode ^= methodName.hashCode();
        }

        /**
         * Retrieves the name of the parameter to be found.
         *
         * @return The name of the parameter to be found.
         */
        public String getName()
        {
            return methodName;
        }

        /**
         * Determine whether or not this key object represents the same key as another object.
         *
         * @return <code>true</code> if the other object is a <code>MethodQueryCacheKey</code> with the same content as
         *         this one.
         */
        public boolean equals(Object o)
        {
            try
            {
                MethodQueryCacheKey otherKey = (MethodQueryCacheKey) o;
                if (!super.equals(otherKey)) return false;
                String otherName = otherKey.getName();
                return otherName.equals(methodName);
            } catch (ClassCastException e)
            {
                return false;
            }
        }

        /**
         * Returns a hash code for this object.
         *
         * @return This object's hash code.
         */
        public int hashCode()
        {
            return super.hashCode();
        }
    }

}

// END OF FILE //