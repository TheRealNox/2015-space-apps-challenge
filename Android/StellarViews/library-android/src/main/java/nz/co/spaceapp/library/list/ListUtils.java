package nz.co.spaceapp.library.list;

import java.util.Collection;

/**
 * Created by Marc Giovannoni on 7/07/14.
 */
public class ListUtils {

    public static <T, E> T find(Collection<T> c, E toCompare, Comparator<E, T> comparator) {
        for (T o : c) {
            if (comparator.compare(toCompare, o)) {
                return o;
            }
        }
        return null;
    }

    public interface Comparator<T, E>  {

        boolean compare(T a, E b);
    }
}
